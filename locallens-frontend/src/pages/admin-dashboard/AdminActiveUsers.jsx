import React from "react";
import { MOCK_ACTIVE_USERS } from "./data/mockData";
import { Clock, MapPin, Compass, Users } from "lucide-react";

function ActiveUserCard({ user }) {
  return (
    <div style={{
      backgroundColor: "var(--white)",
      borderRadius: "14px",
      padding: "20px",
      boxShadow: "var(--card-shadow)",
      border: "1px solid #f0f3f1",
      transition: "transform 0.2s ease, box-shadow 0.2s ease",
      position: "relative",
      overflow: "hidden",
    }}
      onMouseEnter={e => { e.currentTarget.style.transform = "translateY(-2px)"; e.currentTarget.style.boxShadow = "0 8px 28px rgba(0,0,0,0.09)"; }}
      onMouseLeave={e => { e.currentTarget.style.transform = "translateY(0)"; e.currentTarget.style.boxShadow = "var(--card-shadow)"; }}
    >
      {/* Online indicator background */}
      {user.online && (
        <div style={{ position: "absolute", top: 0, right: 0, width: "60px", height: "60px", background: "linear-gradient(225deg, #dcfce7, transparent)", borderRadius: "0 14px 0 0" }} />
      )}

      <div style={{ display: "flex", alignItems: "flex-start", gap: "14px" }}>
        <div style={{ position: "relative", flexShrink: 0 }}>
          <div style={{
            width: "48px", height: "48px", borderRadius: "50%",
            background: user.role === "LOCAL_GUIDE"
              ? "linear-gradient(135deg, #eaf6ef, #d1fae5)"
              : "linear-gradient(135deg, #eff6ff, #dbeafe)",
            display: "flex", alignItems: "center", justifyContent: "center",
            fontSize: "18px", fontWeight: "800",
            color: user.role === "LOCAL_GUIDE" ? "#1d6d4a" : "#1d4ed8",
          }}>
            {user.avatar}
          </div>
          {/* Online dot */}
          <div style={{
            position: "absolute", bottom: "1px", right: "1px",
            width: "12px", height: "12px", borderRadius: "50%",
            backgroundColor: user.online ? "#22c55e" : "#d1d5db",
            border: "2px solid white",
          }} />
        </div>

        <div style={{ flex: 1, minWidth: 0 }}>
          <p style={{ fontSize: "15px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 2px 0", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>{user.name}</p>
          <p style={{ fontSize: "12px", color: "var(--text-muted)", margin: "0 0 10px 0", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>{user.email}</p>

          <div style={{ display: "flex", gap: "8px", flexWrap: "wrap" }}>
            <span style={{
              display: "inline-flex", alignItems: "center", gap: "4px",
              padding: "3px 8px", borderRadius: "20px", fontSize: "11px", fontWeight: "700",
              backgroundColor: user.role === "LOCAL_GUIDE" ? "#eaf6ef" : "#eff6ff",
              color: user.role === "LOCAL_GUIDE" ? "#1d6d4a" : "#1d4ed8",
            }}>
              {user.role === "LOCAL_GUIDE" ? <Compass size={11} /> : <Users size={11} />}
              {user.role === "LOCAL_GUIDE" ? "Guide" : "Traveller"}
            </span>

            <span style={{ display: "inline-flex", alignItems: "center", gap: "4px", padding: "3px 8px", borderRadius: "20px", fontSize: "11px", fontWeight: "700", backgroundColor: user.online ? "#f0fdf4" : "#f3f4f6", color: user.online ? "#16a34a" : "#9ca3af" }}>
              <span style={{ width: "6px", height: "6px", borderRadius: "50%", backgroundColor: user.online ? "#22c55e" : "#d1d5db", display: "inline-block" }} />
              {user.online ? "Online" : "Away"}
            </span>
          </div>
        </div>
      </div>

      <div style={{ marginTop: "14px", paddingTop: "14px", borderTop: "1px solid #f0f3f1", display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <div style={{ display: "flex", alignItems: "center", gap: "4px", fontSize: "12px", color: "var(--text-muted)" }}>
          <Clock size={12} />
          <span>{user.lastSeen}</span>
        </div>
        <div style={{ display: "flex", alignItems: "center", gap: "4px", fontSize: "12px", color: "var(--text-muted)" }}>
          <MapPin size={12} />
          <span>{user.location}</span>
        </div>
      </div>
    </div>
  );
}

function AdminActiveUsers() {
  const onlineCount = MOCK_ACTIVE_USERS.filter(u => u.online).length;
  const guideCount = MOCK_ACTIVE_USERS.filter(u => u.role === "LOCAL_GUIDE").length;
  const travellerCount = MOCK_ACTIVE_USERS.filter(u => u.role === "TRAVELLER").length;

  return (
    <div style={{ maxWidth: "1000px", margin: "0 auto" }}>
      <div style={{ marginBottom: "24px" }}>
        <h1 style={{ fontSize: "22px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Active Users</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Users who have logged in recently or are currently online</p>
      </div>

      {/* Summary cards */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(180px, 1fr))", gap: "16px", marginBottom: "28px" }}>
        {[
          { label: "Total Active", value: MOCK_ACTIVE_USERS.length, color: "#1d6d4a", bg: "#eaf6ef" },
          { label: "Currently Online", value: onlineCount, color: "#16a34a", bg: "#f0fdf4" },
          { label: "Local Guides", value: guideCount, color: "#7c3aed", bg: "#f5f3ff" },
          { label: "Travellers", value: travellerCount, color: "#1d4ed8", bg: "#eff6ff" },
        ].map(card => (
          <div key={card.label} style={{ backgroundColor: "var(--white)", borderRadius: "12px", padding: "20px", boxShadow: "var(--card-shadow)", border: "1px solid #f0f3f1" }}>
            <p style={{ fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", margin: "0 0 8px 0", textTransform: "uppercase", letterSpacing: "0.5px" }}>{card.label}</p>
            <p style={{ fontSize: "28px", fontWeight: "800", color: card.color, margin: 0 }}>{card.value}</p>
          </div>
        ))}
      </div>

      {/* Live indicator banner */}
      <div style={{ backgroundColor: "#f0fdf4", borderRadius: "10px", padding: "12px 18px", marginBottom: "20px", display: "flex", alignItems: "center", gap: "10px", border: "1px solid #bbf7d0" }}>
        <div style={{ width: "10px", height: "10px", borderRadius: "50%", backgroundColor: "#22c55e", animation: "pulse 2s infinite" }} />
        <span style={{ fontSize: "13px", fontWeight: "600", color: "#16a34a" }}>
          {onlineCount} user{onlineCount !== 1 ? "s" : ""} currently online · Data refreshes every 30 seconds
        </span>
      </div>

      {/* Users grid */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))", gap: "16px" }}>
        {MOCK_ACTIVE_USERS.map(user => (
          <ActiveUserCard key={user.id} user={user} />
        ))}
      </div>

      <style>{`
        @keyframes pulse {
          0%, 100% { opacity: 1; transform: scale(1); }
          50% { opacity: 0.5; transform: scale(1.4); }
        }
      `}</style>
    </div>
  );
}

export default AdminActiveUsers;
