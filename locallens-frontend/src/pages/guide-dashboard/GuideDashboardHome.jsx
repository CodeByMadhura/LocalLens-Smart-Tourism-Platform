import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import {
  MapPin, CheckCircle, Clock, XCircle, Star, MessageSquare,
  PlusCircle, User, ArrowRight, TrendingUp
} from "lucide-react";

function GuideDashboardHome() {
  const navigate = useNavigate();
  const { currentUser } = useAuth();

  // Mock statistics data
  const stats = [
    { label: "Total Places", value: 6, icon: <MapPin size={22} />, color: "#1d6d4a", bg: "#eaf6ef" },
    { label: "Approved", value: 3, icon: <CheckCircle size={22} />, color: "#16a34a", bg: "#f0fdf4" },
    { label: "Pending Approval", value: 2, icon: <Clock size={22} />, color: "#d97706", bg: "#fffbeb" },
    { label: "Rejected", value: 1, icon: <XCircle size={22} />, color: "#dc2626", bg: "#fef2f2" },
    { label: "Average Rating", value: "4.8 ★", icon: <Star size={22} />, color: "#ca8a04", bg: "#fefce8" },
    { label: "Total Reviews", value: 156, icon: <MessageSquare size={22} />, color: "#7c3aed", bg: "#f5f3ff" },
  ];

  // Mock recently uploaded places
  const recentPlaces = [
    { id: 1, name: "Hidden Forest Shrine", category: "Cultural", status: "Approved", date: "2026-07-20", rating: 4.9 },
    { id: 2, name: "Trattoria Nonna's Secret", category: "Food", status: "Approved", date: "2026-07-22", rating: 4.8 },
    { id: 3, name: "Blue Lagoon Rock Pools", category: "Nature", status: "Pending", date: "2026-07-25", rating: 0 },
    { id: 4, name: "Grand Temple Ruins", category: "Historical", status: "Rejected", date: "2026-07-26", rating: 0 },
  ];

  const getStatusBadge = (status) => {
    const map = {
      Approved: { bg: "#eafaf1", color: "#16a34a" },
      Pending: { bg: "#fffbeb", color: "#d97706" },
      Rejected: { bg: "#fef2f2", color: "#dc2626" },
    };
    const style = map[status] || { bg: "#f3f4f6", color: "#374151" };
    return (
      <span style={{ padding: "4px 10px", backgroundColor: style.bg, color: style.color, borderRadius: "20px", fontSize: "12px", fontWeight: "700" }}>
        {status}
      </span>
    );
  };

  return (
    <div style={{ maxWidth: "1100px", margin: "0 auto" }}>
      {/* Welcome Banner */}
      <div style={{
        background: "linear-gradient(135deg, var(--primary-green) 0%, var(--dark-green) 100%)",
        borderRadius: "16px",
        padding: "28px 32px",
        marginBottom: "32px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        flexWrap: "wrap",
        gap: "16px",
      }}>
        <div>
          <h1 style={{ color: "white", fontSize: "24px", fontWeight: "800", margin: "0 0 6px 0" }}>
            👋 Welcome back, {currentUser?.firstName}!
          </h1>
          <p style={{ color: "rgba(255,255,255,0.75)", fontSize: "14px", margin: 0 }}>
            Manage your places, reviews, and profile from your Local Guide dashboard.
          </p>
        </div>
        <div style={{ display: "flex", gap: "12px", flexWrap: "wrap" }}>
          <button
            onClick={() => navigate("/guide-dashboard/add-place")}
            style={{ display: "flex", alignItems: "center", gap: "8px", padding: "10px 20px", backgroundColor: "white", color: "var(--primary-green)", borderRadius: "8px", fontWeight: "700", fontSize: "14px", border: "none", cursor: "pointer" }}
          >
            <PlusCircle size={18} /> Add New Place
          </button>
          <button
            onClick={() => navigate("/guide-dashboard/profile")}
            style={{ display: "flex", alignItems: "center", gap: "8px", padding: "10px 20px", backgroundColor: "rgba(255,255,255,0.15)", color: "white", borderRadius: "8px", fontWeight: "700", fontSize: "14px", border: "1px solid rgba(255,255,255,0.3)", cursor: "pointer" }}
          >
            <User size={18} /> Edit Profile
          </button>
        </div>
      </div>

      {/* Stats Grid */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(170px, 1fr))", gap: "20px", marginBottom: "32px" }}>
        {stats.map((stat, i) => (
          <div key={i} style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "20px" }}>
            <div style={{ width: "44px", height: "44px", backgroundColor: stat.bg, borderRadius: "10px", display: "flex", alignItems: "center", justifyContent: "center", marginBottom: "12px", color: stat.color }}>
              {stat.icon}
            </div>
            <p style={{ fontSize: "13px", color: "var(--text-muted)", margin: "0 0 4px 0", fontWeight: "600" }}>{stat.label}</p>
            <p style={{ fontSize: "26px", fontWeight: "800", color: "var(--text-dark)", margin: 0 }}>{stat.value}</p>
          </div>
        ))}
      </div>

      {/* Recent Places Table */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", overflow: "hidden" }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", padding: "20px 24px", borderBottom: "1px solid var(--border-color)" }}>
          <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
            <TrendingUp size={20} color="var(--primary-green)" />
            <h2 style={{ fontSize: "17px", fontWeight: "700", color: "var(--text-dark)", margin: 0 }}>Latest Uploaded Places</h2>
          </div>
          <button
            onClick={() => navigate("/guide-dashboard/places")}
            style={{ display: "flex", alignItems: "center", gap: "6px", color: "var(--primary-green)", fontWeight: "700", fontSize: "13px", background: "none", border: "none", cursor: "pointer" }}
          >
            View All <ArrowRight size={16} />
          </button>
        </div>

        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ backgroundColor: "#f9fafb" }}>
              <th style={{ padding: "14px 24px", textAlign: "left", fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", textTransform: "uppercase", letterSpacing: "0.5px" }}>Place Name</th>
              <th style={{ padding: "14px 24px", textAlign: "left", fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", textTransform: "uppercase", letterSpacing: "0.5px" }}>Category</th>
              <th style={{ padding: "14px 24px", textAlign: "left", fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", textTransform: "uppercase", letterSpacing: "0.5px" }}>Date Added</th>
              <th style={{ padding: "14px 24px", textAlign: "left", fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", textTransform: "uppercase", letterSpacing: "0.5px" }}>Rating</th>
              <th style={{ padding: "14px 24px", textAlign: "left", fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", textTransform: "uppercase", letterSpacing: "0.5px" }}>Status</th>
            </tr>
          </thead>
          <tbody>
            {recentPlaces.map((place) => (
              <tr key={place.id} style={{ borderTop: "1px solid #f0f3f1" }}>
                <td style={{ padding: "16px 24px", fontWeight: "700", color: "var(--text-dark)", fontSize: "14px" }}>{place.name}</td>
                <td style={{ padding: "16px 24px", fontSize: "13px", color: "var(--text-medium)" }}>
                  <span style={{ backgroundColor: "#eaf6ef", color: "var(--primary-green)", padding: "3px 8px", borderRadius: "4px", fontSize: "12px", fontWeight: "700" }}>{place.category}</span>
                </td>
                <td style={{ padding: "16px 24px", fontSize: "13px", color: "var(--text-muted)" }}>{place.date}</td>
                <td style={{ padding: "16px 24px", fontSize: "13px", color: "#ca8a04", fontWeight: "700" }}>
                  {place.rating > 0 ? `★ ${place.rating}` : "—"}
                </td>
                <td style={{ padding: "16px 24px" }}>{getStatusBadge(place.status)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default GuideDashboardHome;
