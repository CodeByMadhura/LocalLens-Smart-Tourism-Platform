import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import {
  Users, MapPin, CheckCircle, XCircle, Clock, Flag, Activity,
  TrendingUp, PlusCircle, User, ArrowRight, UserCheck, Eye
} from "lucide-react";
import {
  MOCK_STATS, MOCK_RECENT_ACTIVITIES, MOCK_LATEST_PLACES, MOCK_LATEST_USERS
} from "./data/mockData";

// Animated counter hook
function useCounter(target, duration = 1200) {
  const [count, setCount] = useState(0);
  useEffect(() => {
    const step = target / (duration / 16);
    let current = 0;
    const timer = setInterval(() => {
      current += step;
      if (current >= target) { setCount(target); clearInterval(timer); }
      else setCount(Math.floor(current));
    }, 16);
    return () => clearInterval(timer);
  }, [target, duration]);
  return count;
}

function StatCard({ label, value, icon, color, bg, trend }) {
  const animated = useCounter(typeof value === "number" ? value : 0);
  return (
    <div style={{
      backgroundColor: "var(--white)",
      borderRadius: "14px",
      padding: "22px",
      boxShadow: "var(--card-shadow)",
      display: "flex",
      flexDirection: "column",
      gap: "12px",
      transition: "transform 0.2s ease, box-shadow 0.2s ease",
      cursor: "default",
      border: "1px solid #f0f3f1",
    }}
      onMouseEnter={e => { e.currentTarget.style.transform = "translateY(-2px)"; e.currentTarget.style.boxShadow = "0 8px 30px rgba(0,0,0,0.09)"; }}
      onMouseLeave={e => { e.currentTarget.style.transform = "translateY(0)"; e.currentTarget.style.boxShadow = "var(--card-shadow)"; }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
        <div style={{ width: "46px", height: "46px", backgroundColor: bg, borderRadius: "12px", display: "flex", alignItems: "center", justifyContent: "center", color, flexShrink: 0 }}>
          {icon}
        </div>
        {trend !== undefined && (
          <span style={{ fontSize: "12px", fontWeight: "700", color: trend >= 0 ? "#16a34a" : "#dc2626", backgroundColor: trend >= 0 ? "#f0fdf4" : "#fef2f2", padding: "3px 8px", borderRadius: "20px" }}>
            {trend >= 0 ? "+" : ""}{trend}%
          </span>
        )}
      </div>
      <div>
        <p style={{ fontSize: "13px", color: "var(--text-muted)", margin: "0 0 4px 0", fontWeight: "600" }}>{label}</p>
        <p style={{ fontSize: "28px", fontWeight: "800", color: "var(--text-dark)", margin: 0 }}>
          {typeof value === "number" ? animated.toLocaleString() : value}
        </p>
      </div>
    </div>
  );
}

function StatusBadge({ status }) {
  const map = {
    PENDING: { bg: "#fffbeb", color: "#d97706", label: "Pending" },
    APPROVED: { bg: "#f0fdf4", color: "#16a34a", label: "Approved" },
    REJECTED: { bg: "#fef2f2", color: "#dc2626", label: "Rejected" },
    LOCAL_GUIDE: { bg: "#eaf6ef", color: "#1d6d4a", label: "Guide" },
    TRAVELLER: { bg: "#eff6ff", color: "#1d4ed8", label: "Traveller" },
  };
  const s = map[status] || { bg: "#f3f4f6", color: "#6b7280", label: status };
  return (
    <span style={{ padding: "3px 10px", backgroundColor: s.bg, color: s.color, borderRadius: "20px", fontSize: "12px", fontWeight: "700", whiteSpace: "nowrap" }}>
      {s.label}
    </span>
  );
}

const activityIconMap = {
  MapPin: <MapPin size={16} />,
  User: <User size={16} />,
  Flag: <Flag size={16} />,
  CheckCircle: <CheckCircle size={16} />,
  XCircle: <XCircle size={16} />,
  UserPlus: <UserCheck size={16} />,
};

const activityColorMap = {
  PLACE_REQUEST: { bg: "#eff6ff", color: "#1d4ed8" },
  USER_REGISTER: { bg: "#f0fdf4", color: "#16a34a" },
  REPORT: { bg: "#fef2f2", color: "#dc2626" },
  PLACE_APPROVED: { bg: "#f0fdf4", color: "#16a34a" },
  PLACE_REJECTED: { bg: "#fef2f2", color: "#dc2626" },
};

function AdminDashboardHome() {
  const navigate = useNavigate();

  const statsConfig = [
    { label: "Total Users", value: MOCK_STATS.totalUsers, icon: <Users size={22} />, color: "#1d6d4a", bg: "#eaf6ef", trend: 12 },
    { label: "Total Travellers", value: MOCK_STATS.totalTravellers, icon: <User size={22} />, color: "#1d4ed8", bg: "#eff6ff", trend: 8 },
    { label: "Total Local Guides", value: MOCK_STATS.totalGuides, icon: <UserCheck size={22} />, color: "#7c3aed", bg: "#f5f3ff", trend: 15 },
    { label: "Pending Places", value: MOCK_STATS.pendingPlaces, icon: <Clock size={22} />, color: "#d97706", bg: "#fffbeb", trend: -3 },
    { label: "Approved Places", value: MOCK_STATS.approvedPlaces, icon: <CheckCircle size={22} />, color: "#16a34a", bg: "#f0fdf4", trend: 22 },
    { label: "Rejected Places", value: MOCK_STATS.rejectedPlaces, icon: <XCircle size={22} />, color: "#dc2626", bg: "#fef2f2", trend: -5 },
    { label: "Reported Guides", value: MOCK_STATS.reportedGuides, icon: <Flag size={22} />, color: "#ea580c", bg: "#fff7ed", trend: 2 },
    { label: "Active Users", value: MOCK_STATS.activeUsers, icon: <Activity size={22} />, color: "#0891b2", bg: "#ecfeff", trend: 18 },
  ];

  const quickActions = [
    { label: "Approve Places", icon: <CheckCircle size={18} />, path: "/admin-dashboard/place-approval", color: "#1d6d4a", bg: "#eaf6ef" },
    { label: "Manage Users", icon: <Users size={18} />, path: "/admin-dashboard/travellers", color: "#1d4ed8", bg: "#eff6ff" },
    { label: "View Reports", icon: <Flag size={18} />, path: "/admin-dashboard/reported-guides", color: "#dc2626", bg: "#fef2f2" },
    { label: "Analytics", icon: <TrendingUp size={18} />, path: "/admin-dashboard/analytics", color: "#7c3aed", bg: "#f5f3ff" },
  ];

  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto" }}>

      {/* Welcome Banner */}
      <div style={{
        background: "linear-gradient(135deg, var(--primary-green) 0%, var(--dark-green) 60%, #0a1f12 100%)",
        borderRadius: "16px",
        padding: "28px 32px",
        marginBottom: "28px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        flexWrap: "wrap",
        gap: "16px",
        position: "relative",
        overflow: "hidden",
      }}>
        <div style={{ position: "absolute", top: "-20px", right: "-20px", width: "180px", height: "180px", borderRadius: "50%", background: "rgba(255,255,255,0.04)" }} />
        <div style={{ position: "absolute", bottom: "-40px", right: "120px", width: "120px", height: "120px", borderRadius: "50%", background: "rgba(223,176,91,0.08)" }} />
        <div style={{ position: "relative" }}>
          <div style={{ display: "flex", alignItems: "center", gap: "10px", marginBottom: "8px" }}>
            <div style={{ padding: "4px 10px", backgroundColor: "rgba(223,176,91,0.2)", borderRadius: "20px", border: "1px solid rgba(223,176,91,0.3)" }}>
              <span style={{ color: "#dfb05b", fontSize: "11px", fontWeight: "700" }}>ADMIN PANEL</span>
            </div>
          </div>
          <h1 style={{ color: "white", fontSize: "24px", fontWeight: "800", margin: "0 0 6px 0" }}>
            Good day, Admin! 👋
          </h1>
          <p style={{ color: "rgba(255,255,255,0.7)", fontSize: "14px", margin: 0 }}>
            Here's what's happening on LocalLens today.
          </p>
        </div>
        <div style={{ display: "flex", gap: "12px", flexWrap: "wrap", position: "relative" }}>
          {quickActions.map((action) => (
            <button
              key={action.label}
              onClick={() => navigate(action.path)}
              style={{ display: "flex", alignItems: "center", gap: "8px", padding: "9px 16px", backgroundColor: "rgba(255,255,255,0.12)", color: "white", borderRadius: "8px", fontWeight: "600", fontSize: "13px", border: "1px solid rgba(255,255,255,0.2)", cursor: "pointer", transition: "all 0.2s" }}
              onMouseEnter={e => { e.currentTarget.style.backgroundColor = "rgba(255,255,255,0.22)"; }}
              onMouseLeave={e => { e.currentTarget.style.backgroundColor = "rgba(255,255,255,0.12)"; }}
            >
              {action.icon} {action.label}
            </button>
          ))}
        </div>
      </div>

      {/* Stats Grid */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))", gap: "18px", marginBottom: "28px" }}>
        {statsConfig.map((stat) => (
          <StatCard key={stat.label} {...stat} />
        ))}
      </div>

      {/* Bottom 2-column layout */}
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "24px" }}>

        {/* Recent Activities */}
        <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", boxShadow: "var(--card-shadow)", overflow: "hidden", border: "1px solid #f0f3f1" }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "18px 22px", borderBottom: "1px solid #f0f3f1" }}>
            <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
              <Activity size={18} color="var(--primary-green)" />
              <h2 style={{ fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", margin: 0 }}>Recent Activities</h2>
            </div>
          </div>
          <div style={{ padding: "8px 0" }}>
            {MOCK_RECENT_ACTIVITIES.map((activity) => {
              const color = activityColorMap[activity.type] || { bg: "#f3f4f6", color: "#6b7280" };
              return (
                <div key={activity.id} style={{ display: "flex", alignItems: "flex-start", gap: "12px", padding: "12px 22px", borderBottom: "1px solid #f9fafb" }}>
                  <div style={{ width: "32px", height: "32px", borderRadius: "8px", backgroundColor: color.bg, color: color.color, display: "flex", alignItems: "center", justifyContent: "center", flexShrink: 0 }}>
                    {activityIconMap[activity.icon]}
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <p style={{ fontSize: "13px", color: "var(--text-dark)", margin: "0 0 2px 0", fontWeight: "500", lineHeight: 1.4 }}>{activity.message}</p>
                    <span style={{ fontSize: "11px", color: "var(--text-muted)" }}>{activity.time}</span>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: "24px" }}>
          {/* Latest Place Requests */}
          <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", boxShadow: "var(--card-shadow)", overflow: "hidden", border: "1px solid #f0f3f1" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "18px 22px", borderBottom: "1px solid #f0f3f1" }}>
              <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                <MapPin size={18} color="var(--primary-green)" />
                <h2 style={{ fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", margin: 0 }}>Latest Place Requests</h2>
              </div>
              <button onClick={() => navigate("/admin-dashboard/place-approval")} style={{ display: "flex", alignItems: "center", gap: "4px", color: "var(--primary-green)", fontWeight: "700", fontSize: "12px", background: "none", border: "none", cursor: "pointer" }}>
                View All <ArrowRight size={14} />
              </button>
            </div>
            <div>
              {MOCK_LATEST_PLACES.map((place) => (
                <div key={place.id} style={{ display: "flex", alignItems: "center", gap: "12px", padding: "12px 22px", borderBottom: "1px solid #f9fafb" }}>
                  <img src={place.img} alt={place.name} style={{ width: "40px", height: "40px", borderRadius: "8px", objectFit: "cover", flexShrink: 0 }} />
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <p style={{ fontSize: "13px", fontWeight: "700", color: "var(--text-dark)", margin: "0 0 2px 0", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>{place.name}</p>
                    <p style={{ fontSize: "12px", color: "var(--text-muted)", margin: 0 }}>{place.guide} · {place.city}</p>
                  </div>
                  <StatusBadge status={place.status} />
                </div>
              ))}
            </div>
          </div>

          {/* Latest Registered Users */}
          <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", boxShadow: "var(--card-shadow)", overflow: "hidden", border: "1px solid #f0f3f1" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "18px 22px", borderBottom: "1px solid #f0f3f1" }}>
              <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                <Users size={18} color="var(--primary-green)" />
                <h2 style={{ fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", margin: 0 }}>Latest Users</h2>
              </div>
              <button onClick={() => navigate("/admin-dashboard/travellers")} style={{ display: "flex", alignItems: "center", gap: "4px", color: "var(--primary-green)", fontWeight: "700", fontSize: "12px", background: "none", border: "none", cursor: "pointer" }}>
                View All <ArrowRight size={14} />
              </button>
            </div>
            <div>
              {MOCK_LATEST_USERS.map((user) => (
                <div key={user.id} style={{ display: "flex", alignItems: "center", gap: "12px", padding: "12px 22px", borderBottom: "1px solid #f9fafb" }}>
                  <div style={{ width: "36px", height: "36px", borderRadius: "50%", backgroundColor: user.role === "LOCAL_GUIDE" ? "#eaf6ef" : "#eff6ff", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", fontSize: "14px", color: user.role === "LOCAL_GUIDE" ? "#1d6d4a" : "#1d4ed8", flexShrink: 0 }}>
                    {user.avatar}
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <p style={{ fontSize: "13px", fontWeight: "700", color: "var(--text-dark)", margin: "0 0 2px 0" }}>{user.name}</p>
                    <p style={{ fontSize: "12px", color: "var(--text-muted)", margin: 0 }}>{user.email}</p>
                  </div>
                  <StatusBadge status={user.role} />
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      <style>{`
        @media (max-width: 900px) {
          .admin-home-grid { grid-template-columns: 1fr !important; }
        }
      `}</style>
    </div>
  );
}

export default AdminDashboardHome;
