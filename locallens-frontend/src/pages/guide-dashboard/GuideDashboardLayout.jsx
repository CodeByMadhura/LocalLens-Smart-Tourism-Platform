import React, { useState } from "react";
import { Outlet, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import {
  LayoutDashboard, User, MapPin, PlusCircle,
  Star, LogOut, Menu, X, ChevronRight
} from "lucide-react";

function GuideDashboardLayout() {
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const menuItems = [
    { path: "/guide-dashboard", label: "Dashboard", icon: <LayoutDashboard size={20} />, exact: true },
    { path: "/guide-dashboard/profile", label: "My Profile", icon: <User size={20} /> },
    { path: "/guide-dashboard/places", label: "My Places", icon: <MapPin size={20} /> },
    { path: "/guide-dashboard/add-place", label: "Add New Place", icon: <PlusCircle size={20} /> },
    { path: "/guide-dashboard/reviews", label: "Ratings & Reviews", icon: <Star size={20} /> },
  ];

  const sidebarStyle = {
    width: "260px",
    flexShrink: 0,
    backgroundColor: "var(--dark-green)",
    display: "flex",
    flexDirection: "column",
    minHeight: "100vh",
    position: "sticky",
    top: 0,
    transition: "transform 0.3s ease",
  };

  return (
    <div style={{ display: "flex", minHeight: "100vh", backgroundColor: "var(--light-green-bg)" }}>

      {/* ===== SIDEBAR ===== */}
      {/* Mobile overlay */}
      {sidebarOpen && (
        <div
          onClick={() => setSidebarOpen(false)}
          style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.5)", zIndex: 40 }}
        />
      )}

      <aside style={{
        ...sidebarStyle,
        position: "fixed",
        left: 0,
        top: 0,
        bottom: 0,
        zIndex: 50,
        transform: sidebarOpen ? "translateX(0)" : "translateX(-100%)",
        // On desktop, always show
        "@media (min-width: 1024px)": { transform: "translateX(0)" },
      }}
        className="guide-sidebar"
      >
        {/* Sidebar Logo */}
        <div style={{ padding: "24px 24px 16px", borderBottom: "1px solid rgba(255,255,255,0.1)" }}>
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
              <div style={{ width: "34px", height: "34px", backgroundColor: "rgba(255,255,255,0.15)", borderRadius: "8px", display: "flex", alignItems: "center", justifyContent: "center" }}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M12 2C8.13 2 5 5.13 5 9C5 14.25 12 22 12 22C12 22 19 14.25 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="white"/>
                </svg>
              </div>
              <span style={{ color: "white", fontSize: "18px", fontWeight: "800" }}>LocalLens</span>
            </div>
            <button onClick={() => setSidebarOpen(false)} style={{ color: "rgba(255,255,255,0.6)", background: "none", border: "none", cursor: "pointer", display: "flex" }} className="sidebar-close-btn">
              <X size={20} />
            </button>
          </div>

          {/* Guide info badge */}
          <div style={{ marginTop: "16px", display: "flex", alignItems: "center", gap: "12px" }}>
            <div style={{ width: "40px", height: "40px", borderRadius: "50%", backgroundColor: "rgba(255,255,255,0.2)", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", color: "white", fontSize: "16px", flexShrink: 0 }}>
              {currentUser?.firstName?.charAt(0)?.toUpperCase()}
            </div>
            <div>
              <p style={{ color: "white", fontWeight: "700", fontSize: "14px", margin: 0 }}>{currentUser?.firstName} {currentUser?.lastName}</p>
              <span style={{ display: "inline-block", padding: "1px 6px", backgroundColor: "rgba(255,255,255,0.15)", color: "rgba(255,255,255,0.8)", borderRadius: "4px", fontSize: "10px", fontWeight: "700", marginTop: "2px" }}>Local Guide</span>
            </div>
          </div>
        </div>

        {/* Navigation Menu */}
        <nav style={{ flex: 1, padding: "16px 12px", overflowY: "auto" }}>
          <p style={{ color: "rgba(255,255,255,0.4)", fontSize: "10px", fontWeight: "700", textTransform: "uppercase", letterSpacing: "1px", padding: "0 12px", marginBottom: "8px" }}>Menu</p>
          {menuItems.map((item) => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.exact}
              onClick={() => setSidebarOpen(false)}
              style={({ isActive }) => ({
                display: "flex",
                alignItems: "center",
                gap: "12px",
                padding: "11px 14px",
                borderRadius: "8px",
                marginBottom: "4px",
                fontSize: "14px",
                fontWeight: "600",
                color: isActive ? "white" : "rgba(255,255,255,0.65)",
                backgroundColor: isActive ? "rgba(255,255,255,0.15)" : "transparent",
                textDecoration: "none",
                transition: "all 0.15s ease",
              })}
            >
              {item.icon}
              {item.label}
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div style={{ padding: "16px", borderTop: "1px solid rgba(255,255,255,0.1)" }}>
          <button
            onClick={handleLogout}
            style={{ display: "flex", alignItems: "center", gap: "10px", padding: "11px 14px", borderRadius: "8px", width: "100%", color: "rgba(255,255,255,0.65)", fontSize: "14px", fontWeight: "600", background: "none", border: "none", cursor: "pointer", transition: "all 0.15s" }}
            onMouseOver={e => { e.currentTarget.style.backgroundColor = "rgba(239,68,68,0.2)"; e.currentTarget.style.color = "#f87171"; }}
            onMouseOut={e => { e.currentTarget.style.backgroundColor = "transparent"; e.currentTarget.style.color = "rgba(255,255,255,0.65)"; }}
          >
            <LogOut size={20} />
            Logout
          </button>
        </div>
      </aside>

      {/* ===== MAIN AREA ===== */}
      <div style={{ flex: 1, display: "flex", flexDirection: "column", marginLeft: "260px" }} className="guide-main">

        {/* Top Navbar */}
        <header style={{ backgroundColor: "var(--white)", borderBottom: "1px solid var(--border-color)", padding: "0 32px", height: "64px", display: "flex", alignItems: "center", justifyContent: "space-between", position: "sticky", top: 0, zIndex: 30, boxShadow: "0 1px 4px rgba(0,0,0,0.04)" }}>
          <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
            {/* Hamburger for mobile */}
            <button onClick={() => setSidebarOpen(true)} style={{ background: "none", border: "none", cursor: "pointer", color: "var(--text-medium)", display: "none" }} className="hamburger-btn">
              <Menu size={22} />
            </button>
            <div style={{ display: "flex", alignItems: "center", gap: "8px", fontSize: "13px", color: "var(--text-muted)" }}>
              <span>LocalLens</span>
              <ChevronRight size={14} />
              <span style={{ fontWeight: "700", color: "var(--text-dark)" }}>Guide Dashboard</span>
            </div>
          </div>

          <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
            <div style={{ textAlign: "right" }}>
              <p style={{ margin: 0, fontWeight: "700", color: "var(--text-dark)", fontSize: "14px" }}>{currentUser?.firstName} {currentUser?.lastName}</p>
              <p style={{ margin: 0, fontSize: "11px", color: "var(--text-muted)" }}>{currentUser?.email}</p>
            </div>
            <div style={{ width: "38px", height: "38px", borderRadius: "50%", backgroundColor: "var(--primary-green)", color: "white", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", fontSize: "16px" }}>
              {currentUser?.firstName?.charAt(0)?.toUpperCase()}
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main style={{ flex: 1, padding: "32px" }}>
          <Outlet />
        </main>
      </div>

      {/* Responsive CSS */}
      <style>{`
        @media (min-width: 1024px) {
          .guide-sidebar {
            transform: translateX(0) !important;
          }
          .sidebar-close-btn {
            display: none !important;
          }
          .hamburger-btn {
            display: none !important;
          }
        }
        @media (max-width: 1023px) {
          .guide-main {
            margin-left: 0 !important;
          }
          .hamburger-btn {
            display: flex !important;
          }
        }
        .guide-sidebar nav a:hover {
          background-color: rgba(255,255,255,0.1) !important;
          color: white !important;
        }
      `}</style>
    </div>
  );
}

export default GuideDashboardLayout;
