import React, { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Header() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isLoggedIn, currentUser, isLocalGuide, logout } = useAuth();
  const [showUserMenu, setShowUserMenu] = useState(false);

  const isHome = location.pathname === "/";

  const navItemStyle = (path) => ({
    display: "flex",
    alignItems: "center",
    padding: "8px 16px",
    borderRadius: "20px",
    fontSize: "14px",
    fontWeight: "600",
    color: location.pathname === path
      ? "var(--primary-green)"
      : (isHome ? "var(--white)" : "var(--text-medium)"),
    backgroundColor: location.pathname === path ? "#eaf6ef" : "transparent",
    cursor: "pointer",
    transition: "all 0.2s ease",
    textDecoration: "none",
  });

  const headerStyle = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "16px 8%",
    backgroundColor: isHome ? "transparent" : "var(--white)",
    borderBottom: isHome ? "none" : "1px solid #f0f3f1",
    position: isHome ? "absolute" : "sticky",
    top: 0,
    left: 0,
    right: 0,
    zIndex: 1000,
    transition: "all 0.3s ease",
  };

  const logoStyle = {
    display: "flex",
    alignItems: "center",
    fontSize: "20px",
    fontWeight: "800",
    color: isHome ? "var(--white)" : "var(--primary-green)",
    cursor: "pointer",
    textDecoration: "none",
  };

  const loginBtnStyle = {
    display: "flex",
    alignItems: "center",
    gap: "6px",
    padding: "8px 16px",
    borderRadius: "8px",
    border: isHome ? "1px solid rgba(255,255,255,0.4)" : "1px solid var(--border-color)",
    fontSize: "14px",
    fontWeight: "600",
    color: isHome ? "var(--white)" : "var(--text-medium)",
    backgroundColor: isHome ? "transparent" : "var(--white)",
    cursor: "pointer",
    transition: "all 0.2s ease",
    textDecoration: "none",
  };

  const registerBtnStyle = {
    padding: "8px 16px",
    borderRadius: "8px",
    backgroundColor: isHome ? "rgba(255,255,255,0.9)" : "var(--primary-green)",
    color: isHome ? "var(--primary-green)" : "var(--white)",
    fontSize: "14px",
    fontWeight: "700",
    cursor: "pointer",
    textDecoration: "none",
  };

  const handleLogout = () => {
    logout();
    setShowUserMenu(false);
    navigate("/");
  };

  return (
    <header style={headerStyle}>
      {/* Logo */}
      <Link to="/" style={logoStyle}>
        <div style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          width: "34px",
          height: "34px",
          backgroundColor: "var(--primary-green)",
          borderRadius: "8px",
          marginRight: "10px",
          color: "var(--white)",
          flexShrink: 0,
        }}>
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
            <path d="M12 2C8.13 2 5 5.13 5 9C5 14.25 12 22 12 22C12 22 19 14.25 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="white"/>
          </svg>
        </div>
        <span>LocalLens</span>
      </Link>

      {/* Navigation */}
      <nav style={{ display: "flex", gap: "8px" }}>
        <Link to="/" style={navItemStyle("/")}>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style={{ marginRight: "6px" }}>
            <path d="M12 2C6.48 2 2 6.48 2 12S6.48 22 12 22 22 17.52 22 12 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12S7.59 4 12 4 20 7.59 20 12 16.41 20 12 20ZM12 6C8.69 6 6 8.69 6 12S8.69 18 12 18 18 15.31 18 12 15.31 6 12 6ZM12 14C10.9 14 10 13.1 10 12S10.9 10 12 10 14 10.9 14 12 13.1 14 12 14Z" fill="currentColor"/>
          </svg>
          Discover
        </Link>
        <Link to="/places" style={navItemStyle("/places")}>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" style={{ marginRight: "6px" }}>
            <path d="M12 2C8.13 2 5 5.13 5 9C5 13.17 12 22 12 22C12 22 19 13.17 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="currentColor"/>
          </svg>
          Places
        </Link>
        <Link to="/about" style={navItemStyle("/about")}>About Us</Link>
        <Link to="/contact" style={navItemStyle("/contact")}>Contact</Link>
      </nav>

      {/* Auth Buttons / User Menu */}
      <div style={{ display: "flex", alignItems: "center", gap: "12px" }}>
        {isLoggedIn ? (
          <div style={{ position: "relative" }}>
            {/* User Avatar Button */}
            <button
              onClick={() => setShowUserMenu(!showUserMenu)}
              style={{
                display: "flex",
                alignItems: "center",
                gap: "8px",
                padding: "6px 12px",
                borderRadius: "24px",
                border: "1px solid var(--border-color)",
                backgroundColor: "var(--white)",
                cursor: "pointer",
                fontSize: "14px",
                fontWeight: "600",
                color: "var(--text-dark)",
              }}
            >
              <div style={{
                width: "28px",
                height: "28px",
                borderRadius: "50%",
                backgroundColor: "var(--primary-green)",
                color: "white",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                fontWeight: "700",
                fontSize: "13px",
              }}>
                {currentUser?.firstName?.charAt(0)?.toUpperCase()}
              </div>
              {currentUser?.firstName}
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none">
                <path d="M7 10l5 5 5-5" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </button>

            {/* Dropdown Menu */}
            {showUserMenu && (
              <div style={{
                position: "absolute",
                top: "calc(100% + 8px)",
                right: 0,
                backgroundColor: "var(--white)",
                border: "1px solid var(--border-color)",
                borderRadius: "12px",
                boxShadow: "0 8px 24px rgba(0,0,0,0.1)",
                minWidth: "200px",
                overflow: "hidden",
                zIndex: 100,
              }}>
                <div style={{ padding: "16px", borderBottom: "1px solid var(--border-color)" }}>
                  <p style={{ fontWeight: "700", color: "var(--text-dark)", margin: "0 0 2px 0" }}>{currentUser?.firstName} {currentUser?.lastName}</p>
                  <p style={{ fontSize: "12px", color: "var(--text-muted)", margin: 0 }}>{currentUser?.email}</p>
                  <span style={{ display: "inline-block", marginTop: "6px", padding: "2px 8px", backgroundColor: "#eaf6ef", color: "var(--primary-green)", borderRadius: "4px", fontSize: "11px", fontWeight: "700" }}>
                    {currentUser?.role === "LOCAL_GUIDE" ? "Local Guide" : "Traveller"}
                  </span>
                </div>

                {isLocalGuide && (
                  <Link
                    to="/guide-dashboard"
                    onClick={() => setShowUserMenu(false)}
                    style={{ display: "flex", alignItems: "center", gap: "10px", padding: "12px 16px", fontSize: "14px", fontWeight: "600", color: "var(--text-dark)", textDecoration: "none", transition: "background 0.15s" }}
                    onMouseOver={e => e.currentTarget.style.backgroundColor = "#f9fafb"}
                    onMouseOut={e => e.currentTarget.style.backgroundColor = "transparent"}
                  >
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" stroke="var(--primary-green)" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                    My Dashboard
                  </Link>
                )}

                <button
                  onClick={handleLogout}
                  style={{ display: "flex", alignItems: "center", gap: "10px", padding: "12px 16px", fontSize: "14px", fontWeight: "600", color: "#ef4444", width: "100%", textAlign: "left", background: "none", border: "none", cursor: "pointer", transition: "background 0.15s" }}
                  onMouseOver={e => e.currentTarget.style.backgroundColor = "#fef2f2"}
                  onMouseOut={e => e.currentTarget.style.backgroundColor = "transparent"}
                >
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/><polyline points="16 17 21 12 16 7" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/><line x1="21" y1="12" x2="9" y2="12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/></svg>
                  Logout
                </button>
              </div>
            )}
          </div>
        ) : (
          <>
            <Link to="/login" style={loginBtnStyle}>
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none">
                <path d="M12 12C14.21 12 16 10.21 16 8C16 5.79 14.21 4 12 4C9.79 4 8 5.79 8 8C8 10.21 9.79 12 12 12ZM12 14C9.33 14 4 15.34 4 18V20H20V18C20 15.34 14.67 14 12 14Z" fill="currentColor"/>
              </svg>
              Login
            </Link>
            <Link to="/register" style={registerBtnStyle}>
              + Register
            </Link>
          </>
        )}
      </div>
    </header>
  );
}

export default Header;
