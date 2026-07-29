import React, { useState } from "react";
import { Link } from "react-router-dom";
import api, { AUTH_ENDPOINTS } from "../api/api";

function Register() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("TRAVELLER");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  // SVG Icon Components
  const PinIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M12 2C8.13 2 5 5.13 5 9C5 14.25 12 22 12 22C12 22 19 14.25 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="white" />
    </svg>
  );

  const UserIcon = () => (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ color: "var(--text-muted)" }}>
      <path d="M12 12C14.21 12 16 10.21 16 8C16 5.79 14.21 4 12 4C9.79 4 8 5.79 8 8C8 10.21 9.79 12 12 12ZM12 14C9.33 14 4 15.34 4 18V20H20V18C20 15.34 14.67 14 12 14Z" fill="currentColor" />
    </svg>
  );

  const MailIcon = () => (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ color: "var(--text-muted)" }}>
      <path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 8L12 13L4 8V6L12 11L20 6V8Z" fill="currentColor" />
    </svg>
  );

  const PhoneIcon = () => (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ color: "var(--text-muted)" }}>
      <path d="M6.62 10.79C8.06 13.62 10.38 15.93 13.21 17.38L15.41 15.18C15.69 14.9 16.08 14.82 16.43 14.93C17.55 15.3 18.75 15.5 20 15.5C20.55 15.5 21 15.95 21 16.5V20C21 20.55 20.55 21 20 21C8.95 21 0 12.05 0 1C0 .45.45 0 1 0H4.5C5.05 0 5.5 .45 5.5 1C5.5 2.25 5.7 3.45 6.07 4.57C6.18 4.92 6.1 5.31 5.82 5.59L3.62 7.79C5.07 10.63 7.39 12.94 10.23 14.38L12.43 12.18C12.71 11.9 13.1 11.82 13.45 11.93C14.57 12.3 15.77 12.5 17 12.5C17.55 12.5 18 12.95 18 13.5V17C18 17.55 17.55 18 17 18C5.95 18 -3 9.05 -3 -2" fill="currentColor" />
    </svg>
  );

  const LockIcon = () => (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ color: "var(--text-muted)" }}>
      <path d="M18 8H17V6C17 3.24 14.76 1 12 1C9.24 1 7 3.24 7 6V8H6C4.9 8 4 8.9 4 10V20C4 21.1 4.9 22 6 22H18C19.1 22 20 21.1 20 20V10C20 8.9 19.1 8 18 8ZM12 17C10.9 17 10 16.1 10 15C10 13.9 10.9 13 12 13C13.1 13 14 13.9 14 15C14 16.1 13.1 17 12 17ZM15.1 8H8.9V6C8.9 4.29 10.29 2.9 12 2.9C13.71 2.9 15.1 4.29 15.1 6V8Z" fill="currentColor" />
    </svg>
  );

  const EyeIcon = ({ visible }) => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ color: "var(--text-muted)" }}>
      {visible ? (
        <path d="M12 4.5C7 4.5 2.73 7.61 1 12C2.73 16.39 7 19.5 12 19.5C17 19.5 21.27 16.39 23 12C21.27 7.61 17 4.5 12 4.5ZM12 17C9.24 17 7 14.76 7 12C7 9.24 9.24 7 12 7C14.76 7 17 9.24 17 12C17 14.76 14.76 17 12 17ZM12 9C10.34 9 9 10.34 9 12C9 13.66 10.34 15 12 15C13.66 15 15 13.66 15 12C15 10.34 13.66 9 12 9Z" fill="currentColor" />
      ) : (
        <path d="M12 17C14.76 17 17 14.76 17 12C17 9.24 14.76 7 12 7C9.24 7 7 9.24 7 12C7 14.76 9.24 17 12 17ZM2 4.27L4.28 6.55L4.73 7C3.08 8.3 1.78 10 1 12C2.73 16.39 7 19.5 12 19.5C13.8 19.5 15.51 19.06 17.03 18.29L17.48 18.74L19.73 21L21 19.73L3.27 2L2 3.27ZM12 9C12.35 9 12.69 9.07 13 9.19L14.81 11C14.93 11.31 15 11.65 15 12C15 13.66 13.66 15 12 15C11.65 15 11.31 14.93 11 14.81L7.29 11.1C7.88 9.9 9.8 9 12 9ZM12 4.5C14.55 4.5 16.92 5.37 18.89 6.84L17.42 8.31C15.89 7.48 14.01 7 12 7C11.51 7 11.03 7.03 10.56 7.09L9.02 5.55C9.97 5.2 10.97 4.5 12 4.5ZM23 12C22.22 13.9 20.92 15.6 19.27 16.92L17.78 15.43C19.12 14.54 20.27 13.4 21 12C19.88 10.1 18.12 8.54 16.03 7.74L14.54 6.25C17.27 7.15 19.64 8.7 21 10.8C22.27 12 22.27 12 23 12Z" fill="currentColor" />
      )}
    </svg>
  );

  const containerStyle = {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    padding: "80px 24px",
    backgroundColor: "var(--light-green-bg)",
    minHeight: "calc(100vh - 80px)",
  };

  const cardStyle = {
    width: "100%",
    maxWidth: "520px",
    backgroundColor: "var(--white)",
    padding: "48px 40px",
    borderRadius: "16px",
    boxShadow: "var(--card-shadow)",
    textAlign: "center",
  };

  const logoContainerStyle = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: "8px",
    marginBottom: "32px",
  };

  const iconBadgeStyle = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "36px",
    height: "36px",
    backgroundColor: "var(--primary-green)",
    borderRadius: "8px",
  };

  const logoTextStyle = {
    fontSize: "20px",
    fontWeight: "800",
    color: "var(--primary-green)",
  };

  const headingStyle = {
    fontSize: "30px",
    fontWeight: "850",
    color: "#0f172a",
    marginBottom: "8px",
  };

  const textStyle = {
    color: "var(--text-muted)",
    fontSize: "14px",
    marginBottom: "36px",
  };

  const formRowStyle = {
    display: "flex",
    gap: "16px",
    marginBottom: "20px",
  };

  const formGroupStyle = {
    flex: 1,
    textAlign: "left",
    marginBottom: "20px",
  };

  const labelRowStyle = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: "8px",
  };

  const labelStyle = {
    fontSize: "14px",
    fontWeight: "700",
    color: "var(--text-medium)",
  };

  const inputWrapperStyle = {
    position: "relative",
    display: "flex",
    alignItems: "center",
  };

  const inputIconStyle = {
    position: "absolute",
    left: "14px",
    display: "flex",
    alignItems: "center",
    pointerEvents: "none",
  };

  const inputStyle = {
    width: "100%",
    padding: "12px 14px 12px 42px",
    fontSize: "15px",
    border: "1.5px solid var(--border-color)",
    borderRadius: "8px",
    outline: "none",
    color: "var(--text-dark)",
    transition: "border-color 0.2s ease",
  };

  const selectStyle = {
    width: "100%",
    padding: "12px 14px 12px 42px",
    fontSize: "15px",
    border: "1.5px solid var(--border-color)",
    borderRadius: "8px",
    outline: "none",
    color: "var(--text-dark)",
    backgroundColor: "var(--white)",
    cursor: "pointer",
    appearance: "none",
  };

  const eyeToggleStyle = {
    position: "absolute",
    right: "14px",
    display: "flex",
    alignItems: "center",
    cursor: "pointer",
    background: "none",
    border: "none",
    padding: 0,
  };

  const buttonStyle = {
    width: "100%",
    padding: "14px",
    backgroundColor: "var(--primary-green)",
    color: "var(--white)",
    border: "none",
    borderRadius: "8px",
    fontSize: "16px",
    fontWeight: "600",
    cursor: "pointer",
    marginTop: "8px",
    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.05)",
  };

  const signupStyle = {
    marginTop: "24px",
    fontSize: "14px",
    color: "var(--text-muted)",
  };

  const signupLinkStyle = {
    color: "var(--primary-green)",
    fontWeight: "700",
    cursor: "pointer",
  };

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess(false);

    try {
      await api.post(AUTH_ENDPOINTS.REGISTER, {
        firstName,
        lastName,
        email,
        phoneNumber: phone,
        password,
        role
      });

      setSuccess(true);
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data?.error || "Registration failed. Please try again.");
    }
  };

  return (
    <div style={containerStyle}>
      <div style={cardStyle}>
        <div style={logoContainerStyle}>
          <div style={iconBadgeStyle}>
            <PinIcon />
          </div>
          <span style={logoTextStyle}>LocalLens</span>
        </div>

        <h2 style={headingStyle}>Create Account</h2>
        <p style={textStyle}>Join LocalLens to discover and share hidden spots</p>

        {error && (
          <div style={{ backgroundColor: "#fef2f2", color: "#b91c1c", padding: "12px", borderRadius: "8px", marginBottom: "20px", fontSize: "14px", fontWeight: "500" }}>
            {error}
          </div>
        )}

        {success && (
          <div style={{ backgroundColor: "#ecfdf5", color: "#047857", padding: "12px", borderRadius: "8px", marginBottom: "20px", fontSize: "14px", fontWeight: "500" }}>
            Registration successful! Please check your backend console for the verification link.
          </div>
        )}

        <form onSubmit={handleRegisterSubmit}>
          <div style={formRowStyle}>
            <div style={{ ...formGroupStyle, marginBottom: 0 }}>
              <div style={labelRowStyle}>
                <label style={labelStyle}>First Name</label>
              </div>
              <div style={inputWrapperStyle}>
                <div style={inputIconStyle}>
                  <UserIcon />
                </div>
                <input
                  type="text"
                  placeholder="First name"
                  style={inputStyle}
                  value={firstName}
                  onChange={(e) => setFirstName(e.target.value)}
                  required
                />
              </div>
            </div>

            <div style={{ ...formGroupStyle, marginBottom: 0 }}>
              <div style={labelRowStyle}>
                <label style={labelStyle}>Last Name</label>
              </div>
              <div style={inputWrapperStyle}>
                <div style={inputIconStyle}>
                  <UserIcon />
                </div>
                <input
                  type="text"
                  placeholder="Last name"
                  style={inputStyle}
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                  required
                />
              </div>
            </div>
          </div>

          <div style={formGroupStyle}>
            <div style={labelRowStyle}>
              <label style={labelStyle}>Email Address</label>
            </div>
            <div style={inputWrapperStyle}>
              <div style={inputIconStyle}>
                <MailIcon />
              </div>
              <input
                type="email"
                placeholder="you@example.com"
                style={inputStyle}
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
          </div>

          <div style={formGroupStyle}>
            <div style={labelRowStyle}>
              <label style={labelStyle}>Phone Number</label>
            </div>
            <div style={inputWrapperStyle}>
              <div style={inputIconStyle}>
                <PhoneIcon />
              </div>
              <input
                type="tel"
                placeholder="+1 (555) 000-0000"
                style={inputStyle}
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                required
              />
            </div>
          </div>

          <div style={formGroupStyle}>
            <div style={labelRowStyle}>
              <label style={labelStyle}>Password</label>
            </div>
            <div style={inputWrapperStyle}>
              <div style={inputIconStyle}>
                <LockIcon />
              </div>
              <input
                type={showPassword ? "text" : "password"}
                placeholder="••••••••••••"
                style={{ ...inputStyle, paddingRight: "44px" }}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                type="button"
                style={eyeToggleStyle}
                onClick={() => setShowPassword(!showPassword)}
              >
                <EyeIcon visible={showPassword} />
              </button>
            </div>
          </div>

          {/* Custom Card-style Radio Selector for Role */}
          <div style={formGroupStyle}>
            <div style={labelRowStyle}>
              <label style={labelStyle}>Register As</label>
            </div>
            <div style={{ display: "flex", gap: "16px", marginTop: "4px" }}>
              {/* Traveller Card Option */}
              <div
                onClick={() => setRole("TRAVELLER")}
                style={{
                  flex: 1,
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  justifyContent: "center",
                  padding: "20px 12px",
                  borderRadius: "12px",
                  border: `2px solid ${role === "TRAVELLER" ? "var(--primary-green)" : "var(--border-color)"}`,
                  backgroundColor: role === "TRAVELLER" ? "#eaf6ef" : "var(--white)",
                  cursor: "pointer",
                  transition: "all 0.2s ease",
                  position: "relative"
                }}
              >
                <div style={{
                  position: "absolute",
                  top: "12px",
                  right: "12px",
                  width: "16px",
                  height: "16px",
                  borderRadius: "50%",
                  border: `2px solid ${role === "TRAVELLER" ? "var(--primary-green)" : "var(--border-color)"}`,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center"
                }}>
                  {role === "TRAVELLER" && (
                    <div style={{ width: "8px", height: "8px", borderRadius: "50%", backgroundColor: "var(--primary-green)" }} />
                  )}
                </div>

                {/* Backpack SVG */}
                <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke={role === "TRAVELLER" ? "var(--primary-green)" : "var(--text-muted)"} strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" style={{ marginBottom: "10px" }}>
                  <path d="M4 10a4 4 0 0 1 4-4h8a4 4 0 0 1 4 4v10a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V10Z" />
                  <path d="M9 6V4a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v2" />
                  <path d="M8 22V10h8v12" />
                  <path d="M4 15h3" />
                  <path d="M20 15h-3" />
                </svg>

                <span style={{ fontSize: "14px", fontWeight: "700", color: role === "TRAVELLER" ? "var(--primary-green)" : "var(--text-medium)" }}>
                  Traveller
                </span>
              </div>

              {/* Local Guide Card Option */}
              <div
                onClick={() => setRole("LOCAL_GUIDE")}
                style={{
                  flex: 1,
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  justifyContent: "center",
                  padding: "20px 12px",
                  borderRadius: "12px",
                  border: `2px solid ${role === "LOCAL_GUIDE" ? "var(--primary-green)" : "var(--border-color)"}`,
                  backgroundColor: role === "LOCAL_GUIDE" ? "#eaf6ef" : "var(--white)",
                  cursor: "pointer",
                  transition: "all 0.2s ease",
                  position: "relative"
                }}
              >
                <div style={{
                  position: "absolute",
                  top: "12px",
                  right: "12px",
                  width: "16px",
                  height: "16px",
                  borderRadius: "50%",
                  border: `2px solid ${role === "LOCAL_GUIDE" ? "var(--primary-green)" : "var(--border-color)"}`,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center"
                }}>
                  {role === "LOCAL_GUIDE" && (
                    <div style={{ width: "8px", height: "8px", borderRadius: "50%", backgroundColor: "var(--primary-green)" }} />
                  )}
                </div>

                {/* Compass/Location Pin SVG */}
                <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke={role === "LOCAL_GUIDE" ? "var(--primary-green)" : "var(--text-muted)"} strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" style={{ marginBottom: "10px" }}>
                  <path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7Z" />
                  <circle cx="12" cy="9" r="3" />
                </svg>

                <span style={{ fontSize: "14px", fontWeight: "700", color: role === "LOCAL_GUIDE" ? "var(--primary-green)" : "var(--text-medium)" }}>
                  Local Guide
                </span>
              </div>
            </div>
          </div>

          <button type="submit" style={buttonStyle}>
            Create Account
          </button>
        </form>

        <p style={signupStyle}>
          Already have an account?{" "}
          <Link to="/login" style={signupLinkStyle}>
            Sign In
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Register;
