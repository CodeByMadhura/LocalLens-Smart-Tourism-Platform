import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api, { AUTH_ENDPOINTS } from "../api/api";
import { useAuth } from "../context/AuthContext";

function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();

  const [verificationMethod, setVerificationMethod] = useState("email");
  const [target, setTarget] = useState(""); // email or phone value
  const [otp, setOtp] = useState("");
  const [otpSent, setOtpSent] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const handleMethodSwitch = (method) => {
    setVerificationMethod(method);
    setTarget("");
    setOtp("");
    setOtpSent(false);
    setError("");
    setSuccessMessage("");
  };

  const handleSendOtp = async () => {
    setError("");
    setSuccessMessage("");

    if (!target.trim()) {
      setError(verificationMethod === "email" ? "Please enter your email address." : "Please enter your phone number.");
      return;
    }

    setLoading(true);
    try {
      await api.post(AUTH_ENDPOINTS.SEND_OTP, {
        target: target.trim(),
        method: verificationMethod,
      });
      setOtpSent(true);
      setSuccessMessage(
        verificationMethod === "email"
          ? "OTP sent to your email. Please check your inbox."
          : "OTP sent. Check the backend console (SMS simulation)."
      );
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data?.error || "Failed to send OTP. Please try again.";
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Step 1: If OTP not yet sent, send it first
    if (!otpSent) {
      await handleSendOtp();
      return;
    }

    // Step 2: Verify OTP and login
    if (!otp.trim()) {
      setError("Please enter the OTP code.");
      return;
    }

    setLoading(true);
    try {
      const response = await api.post(AUTH_ENDPOINTS.LOGIN_OTP, {
        target: target.trim(),
        otp: otp.trim(),
        method: verificationMethod,
      });

      const data = response.data;

      // Save user data in auth context (also saves to localStorage)
      const userData = {
        id: data.id,
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        role: data.role,
        profileCompleted: data.profileCompleted,
      };

      login(userData, data.token);

      // Redirect based on role
      if (data.role === "ADMIN") {
        navigate("/admin-dashboard", { replace: true });
      } else if (data.role === "LOCAL_GUIDE") {
        navigate("/guide-dashboard", { replace: true });
      } else {
        navigate("/", { replace: true });
      }
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data?.error || "Login failed. Please check your OTP and try again.";
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  // Styles
  const containerStyle = {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    padding: "60px 24px",
    backgroundColor: "var(--light-green-bg)",
    minHeight: "calc(100vh - 80px)",
  };

  const cardStyle = {
    width: "100%",
    maxWidth: "480px",
    backgroundColor: "var(--white)",
    padding: "48px 40px",
    borderRadius: "16px",
    boxShadow: "0 8px 40px rgba(0,0,0,0.08)",
    textAlign: "center",
  };

  const inputStyle = {
    width: "100%",
    padding: "12px 14px 12px 42px",
    fontSize: "15px",
    border: "1.5px solid var(--border-color)",
    borderRadius: "8px",
    outline: "none",
    color: "var(--text-dark)",
    boxSizing: "border-box",
  };

  const buttonStyle = {
    width: "100%",
    padding: "14px",
    backgroundColor: loading ? "#9dc5b2" : "var(--primary-green)",
    color: "var(--white)",
    border: "none",
    borderRadius: "8px",
    fontSize: "16px",
    fontWeight: "700",
    cursor: loading ? "not-allowed" : "pointer",
    marginTop: "8px",
    transition: "background-color 0.2s ease",
  };

  const tabStyle = (isActive) => ({
    flex: 1,
    padding: "10px 0",
    borderRadius: "8px",
    fontSize: "14px",
    fontWeight: "700",
    color: isActive ? "var(--primary-green)" : "var(--text-muted)",
    backgroundColor: isActive ? "var(--white)" : "transparent",
    border: "none",
    cursor: "pointer",
  });

  return (
    <div style={containerStyle}>
      <div style={cardStyle}>
        {/* Logo */}
        <div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: "8px", marginBottom: "28px" }}>
          <div style={{ width: "36px", height: "36px", backgroundColor: "var(--primary-green)", borderRadius: "8px", display: "flex", alignItems: "center", justifyContent: "center" }}>
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none"><path d="M12 2C8.13 2 5 5.13 5 9C5 14.25 12 22 12 22C12 22 19 14.25 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="white"/></svg>
          </div>
          <span style={{ fontSize: "20px", fontWeight: "800", color: "var(--primary-green)" }}>LocalLens</span>
        </div>

        <h2 style={{ fontSize: "28px", fontWeight: "800", color: "#0f172a", marginBottom: "8px" }}>Welcome Back</h2>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", marginBottom: "32px" }}>
          Sign in to continue. An OTP will be sent to verify your identity.
        </p>

        {/* Error & Success Messages */}
        {error && (
          <div style={{ backgroundColor: "#fef2f2", color: "#b91c1c", padding: "12px", borderRadius: "8px", marginBottom: "20px", fontSize: "14px", fontWeight: "500", textAlign: "left" }}>
            {error}
          </div>
        )}
        {successMessage && (
          <div style={{ backgroundColor: "#ecfdf5", color: "#047857", padding: "12px", borderRadius: "8px", marginBottom: "20px", fontSize: "14px", fontWeight: "500", textAlign: "left" }}>
            {successMessage}
          </div>
        )}

        {/* Method Tabs: Email / Phone */}
        <div style={{ display: "flex", backgroundColor: "#f0f4f2", borderRadius: "10px", padding: "4px", marginBottom: "28px" }}>
          <button type="button" onClick={() => handleMethodSwitch("email")} style={tabStyle(verificationMethod === "email")}>Email</button>
          <button type="button" onClick={() => handleMethodSwitch("phone")} style={tabStyle(verificationMethod === "phone")}>Phone</button>
        </div>

        <form onSubmit={handleLoginSubmit}>
          {/* Email or Phone Input */}
          {!otpSent && (
            <div style={{ textAlign: "left", marginBottom: "20px" }}>
              <label style={{ display: "block", fontSize: "14px", fontWeight: "700", color: "var(--text-medium)", marginBottom: "8px" }}>
                {verificationMethod === "email" ? "Email Address" : "Phone Number"}
              </label>
              <div style={{ position: "relative", display: "flex", alignItems: "center" }}>
                <div style={{ position: "absolute", left: "14px", color: "var(--text-muted)" }}>
                  {verificationMethod === "email" ? (
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 8L12 13L4 8V6L12 11L20 6V8Z" fill="currentColor"/></svg>
                  ) : (
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><path d="M6.62 10.79a15.15 15.15 0 0 0 6.59 6.59l2.2-2.2a1 1 0 0 1 1.11-.27c1.2.39 2.48.61 3.81.61a1 1 0 0 1 1 1V20a1 1 0 0 1-1 1A17 17 0 0 1 3 4a1 1 0 0 1 1-1h3.5a1 1 0 0 1 1 1c0 1.33.22 2.61.61 3.81a1 1 0 0 1-.27 1.11z" fill="currentColor"/></svg>
                  )}
                </div>
                <input
                  type={verificationMethod === "email" ? "email" : "tel"}
                  placeholder={verificationMethod === "email" ? "guide@example.com" : "+91 98765 43210"}
                  style={inputStyle}
                  value={target}
                  onChange={(e) => setTarget(e.target.value)}
                  required
                  disabled={loading}
                />
              </div>
            </div>
          )}

          {/* OTP Input */}
          {otpSent && (
            <div style={{ textAlign: "left", marginBottom: "20px" }}>
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "8px" }}>
                <label style={{ fontSize: "14px", fontWeight: "700", color: "var(--text-medium)" }}>Enter OTP Code</label>
                <button type="button" onClick={handleSendOtp} disabled={loading} style={{ fontSize: "13px", fontWeight: "700", color: "var(--primary-green)", background: "none", border: "none", cursor: "pointer" }}>
                  Resend OTP
                </button>
              </div>
              <div style={{ position: "relative", display: "flex", alignItems: "center" }}>
                <div style={{ position: "absolute", left: "14px", color: "var(--text-muted)" }}>
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none"><path d="M12 2a4 4 0 0 0-4 4v2H6a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V10a2 2 0 0 0-2-2h-2V6a4 4 0 0 0-4-4zm-2 6V6a2 2 0 0 1 4 0v2H10zm2 7a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z" fill="currentColor"/></svg>
                </div>
                <input
                  type="text"
                  placeholder="6-digit OTP"
                  style={{ ...inputStyle, letterSpacing: "4px", textAlign: "center" }}
                  value={otp}
                  onChange={(e) => setOtp(e.target.value)}
                  maxLength={6}
                  required
                  disabled={loading}
                  autoFocus
                />
              </div>
              <p style={{ fontSize: "12px", color: "var(--text-muted)", marginTop: "6px" }}>
                Sending to: <strong>{target}</strong>
              </p>
            </div>
          )}

          <button type="submit" style={buttonStyle} disabled={loading}>
            {loading ? "Please wait..." : otpSent ? "Login with OTP" : "Send OTP"}
          </button>
        </form>

        <div style={{ marginTop: "24px", fontSize: "14px", color: "var(--text-muted)" }}>
          Don't have an account?{" "}
          <Link to="/register" style={{ color: "var(--primary-green)", fontWeight: "700" }}>Register</Link>
        </div>
      </div>
    </div>
  );
}

export default Login;