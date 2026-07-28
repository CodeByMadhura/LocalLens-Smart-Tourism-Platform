import React, { useState } from "react";
import { Outlet, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import {
  LayoutDashboard, MapPin, Users, Compass, Flag,
  BarChart2, Activity, LogOut, Menu, X, ChevronRight, Shield
} from "lucide-react";

function AdminDashboardLayout() {
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  const menuItems = [
    { path: "/admin-dashboard", label: "Dashboard", icon: <LayoutDashboard size={20} />, exact: true },
    { path: "/admin-dashboard/place-approval", label: "Place Approval", icon: <MapPin size={20} /> },
    { path: "/admin-dashboard/travellers", label: "Travellers", icon: <Users size={20} /> },
    { path: "/admin-dashboard/local-guides", label: "Local Guides", icon: <Compass size={20} /> },
    { path: "/admin-dashboard/reported-guides", label: "Reported Guides", icon: <Flag size={20} /> },
    { path: "/admin-dashboard/analytics", label: "Analytics", icon: <BarChart2 size={20} /> },
    { path: "/admin-dashboard/active-users", label: "Active Users", icon: <Activity size={20} /> },
  ];

  return (
    <div style={{ display: "flex", minHeight: "100vh", backgroundColor: "var(--light-green-bg)" }}>

      {/* Mobile Overlay */}
      {sidebarOpen && (
        <div
          onClick={() => setSidebarOpen(false)}
          style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.5)", zIndex: 40 }}
        />
      )}

      {/* ===== SIDEBAR ===== */}
      <aside
        className="admin-sidebar"
        style={{
          width: "260px",
          flexShrink: 0,
          background: "linear-gradient(180deg, #0c2d1c 0%, #1a4731 100%)",
          display: "flex",
          flexDirection: "column",
          minHeight: "100vh",
          position: "fixed",
          left: 0,
          top: 0,
          bottom: 0,
          zIndex: 50,
          transform: sidebarOpen ? "translateX(0)" : "translateX(-100%)",
          boxShadow: "4px 0 24px rgba(0,0,0,0.15)",
        }}
      >
        {/* Logo Area */}
        <div style={{ padding: "24px 20px 16px", borderBottom: "1px solid rgba(255,255,255,0.08)" }}>
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
              <div style={{ width: "36px", height: "36px", background: "linear-gradient(135deg, #1d6d4a, #dfb05b)", borderRadius: "10px", display: "flex", alignItems: "center", justifyContent: "center", flexShrink: 0 }}>
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
                  <path d="M12 2C8.13 2 5 5.13 5 9C5 14.25 12 22 12 22C12 22 19 14.25 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="white" />
                </svg>
              </div>
              <div>
                <span style={{ color: "white", fontSize: "17px", fontWeight: "800", display: "block", lineHeight: 1.2 }}>LocalLens</span>
                <span style={{ color: "rgba(255,255,255,0.5)", fontSize: "10px", fontWeight: "600", textTransform: "uppercase", letterSpacing: "1px" }}>Admin Panel</span>
              </div>
            </div>
            <button onClick={() => setSidebarOpen(false)} style={{ color: "rgba(255,255,255,0.5)", background: "none", border: "none", cursor: "pointer", display: "flex" }} className="admin-sidebar-close">
              <X size={20} />
            </button>
          </div>

          {/* Admin badge */}
          <div style={{ marginTop: "16px", padding: "10px 12px", backgroundColor: "rgba(223,176,91,0.12)", borderRadius: "10px", border: "1px solid rgba(223,176,91,0.2)", display: "flex", alignItems: "center", gap: "10px" }}>
            <div style={{ width: "36px", height: "36px", borderRadius: "50%", background: "linear-gradient(135deg, #dfb05b, #b8891e)", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", color: "white", fontSize: "14px", flexShrink: 0 }}>
              {currentUser?.firstName?.charAt(0)?.toUpperCase() || "A"}
            </div>
            <div style={{ minWidth: 0 }}>
              <p style={{ color: "white", fontWeight: "700", fontSize: "13px", margin: 0, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>
                {currentUser?.firstName} {currentUser?.lastName}
              </p>
              <div style={{ display: "flex", alignItems: "center", gap: "4px", marginTop: "2px" }}>
                <Shield size={10} color="#dfb05b" />
                <span style={{ color: "#dfb05b", fontSize: "10px", fontWeight: "700" }}>ADMIN</span>
              </div>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <nav style={{ flex: 1, padding: "16px 12px", overflowY: "auto" }}>
          <p style={{ color: "rgba(255,255,255,0.3)", fontSize: "10px", fontWeight: "700", textTransform: "uppercase", letterSpacing: "1.2px", padding: "0 10px", marginBottom: "8px" }}>Main Menu</p>
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
                padding: "10px 12px",
                borderRadius: "8px",
                marginBottom: "3px",
                fontSize: "14px",
                fontWeight: "600",
                color: isActive ? "white" : "rgba(255,255,255,0.6)",
                backgroundColor: isActive ? "rgba(255,255,255,0.12)" : "transparent",
                textDecoration: "none",
                transition: "all 0.15s ease",
                borderLeft: isActive ? "3px solid #dfb05b" : "3px solid transparent",
              })}
            >
              <span style={{ opacity: 0.9 }}>{item.icon}</span>
              {item.label}
            </NavLink>
          ))}
        </nav>

        {/* Logout */}
        <div style={{ padding: "16px", borderTop: "1px solid rgba(255,255,255,0.08)" }}>
          <button
            onClick={handleLogout}
            style={{ display: "flex", alignItems: "center", gap: "10px", padding: "10px 12px", borderRadius: "8px", width: "100%", color: "rgba(255,255,255,0.6)", fontSize: "14px", fontWeight: "600", background: "none", border: "none", cursor: "pointer", transition: "all 0.15s" }}
            onMouseOver={e => { e.currentTarget.style.backgroundColor = "rgba(239,68,68,0.15)"; e.currentTarget.style.color = "#f87171"; }}
            onMouseOut={e => { e.currentTarget.style.backgroundColor = "transparent"; e.currentTarget.style.color = "rgba(255,255,255,0.6)"; }}
          >
            <LogOut size={20} />
            Sign Out
          </button>
        </div>
      </aside>

      {/* ===== MAIN CONTENT ===== */}
      <div className="admin-main" style={{ flex: 1, display: "flex", flexDirection: "column", marginLeft: "260px" }}>

        {/* Top Navbar */}
        <header style={{
          backgroundColor: "var(--white)",
          borderBottom: "1px solid var(--border-color)",
          padding: "0 28px",
          height: "64px",
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          position: "sticky",
          top: 0,
          zIndex: 30,
          boxShadow: "0 1px 8px rgba(0,0,0,0.04)",
        }}>
          <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
            <button onClick={() => setSidebarOpen(true)} className="admin-hamburger" style={{ background: "none", border: "none", cursor: "pointer", color: "var(--text-medium)", display: "none" }}>
              <Menu size={22} />
            </button>
            <div style={{ display: "flex", alignItems: "center", gap: "8px", fontSize: "13px", color: "var(--text-muted)" }}>
              <span style={{ fontWeight: "600" }}>LocalLens</span>
              <ChevronRight size={14} />
              <span style={{ fontWeight: "700", color: "var(--text-dark)" }}>Admin Dashboard</span>
            </div>
          </div>

          <div style={{ display: "flex", alignItems: "center", gap: "14px" }}>
            <div style={{ textAlign: "right" }}>
              <p style={{ margin: 0, fontWeight: "700", color: "var(--text-dark)", fontSize: "14px" }}>
                {currentUser?.firstName} {currentUser?.lastName}
              </p>
              <p style={{ margin: 0, fontSize: "11px", color: "var(--text-muted)" }}>{currentUser?.email}</p>
            </div>
            <div style={{ width: "38px", height: "38px", borderRadius: "50%", background: "linear-gradient(135deg, #dfb05b, #b8891e)", color: "white", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", fontSize: "15px" }}>
              {currentUser?.firstName?.charAt(0)?.toUpperCase() || "A"}
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main style={{ flex: 1, padding: "28px" }}>
          <Outlet />
        </main>
      </div>

      {/* Responsive CSS */}
      <style>{`
        @media (min-width: 1024px) {
          .admin-sidebar {
            transform: translateX(0) !important;
          }
          .admin-sidebar-close {
            display: none !important;
          }
          .admin-hamburger {
            display: none !important;
          }
        }
        @media (max-width: 1023px) {
          .admin-main {
            margin-left: 0 !important;
          }
          .admin-hamburger {
            display: flex !important;
          }
        }
        .admin-sidebar nav a:hover {
          background-color: rgba(255,255,255,0.08) !important;
          color: white !important;
        }
      `}</style>
    </div>
  );
}

export default AdminDashboardLayout;
