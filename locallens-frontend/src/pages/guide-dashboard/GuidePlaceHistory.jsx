import React from "react";
import { CheckCircle, Clock, XCircle, AlertCircle } from "lucide-react";

function GuidePlaceHistory() {
  const history = [
    { id: 1, placeName: "Hidden Forest Shrine", event: "Submitted for Review", date: "2026-07-20", status: "success", remark: "Place submitted successfully and under admin review." },
    { id: 2, placeName: "Hidden Forest Shrine", event: "Approved by Admin", date: "2026-07-21", status: "success", remark: "Congratulations! Your place has been approved and is now publicly visible." },
    { id: 3, placeName: "Trattoria Nonna's Secret", event: "Submitted for Review", date: "2026-07-22", status: "success", remark: "" },
    { id: 4, placeName: "Trattoria Nonna's Secret", event: "Approved by Admin", date: "2026-07-23", status: "success", remark: "Approved. Great content!" },
    { id: 5, placeName: "Blue Lagoon Rock Pools", event: "Submitted for Review", date: "2026-07-25", status: "pending", remark: "" },
    { id: 6, placeName: "Grand Temple Ruins", event: "Submitted for Review", date: "2026-07-26", status: "rejected", remark: "Admin Remark: Insufficient images. Please upload higher quality photos (min 3) and resubmit." },
  ];

  const getIcon = (status) => {
    if (status === "success") return <CheckCircle size={22} color="white" />;
    if (status === "pending") return <Clock size={22} color="white" />;
    if (status === "rejected") return <XCircle size={22} color="white" />;
    return <AlertCircle size={22} color="white" />;
  };

  const getBgColor = (status) => {
    if (status === "success") return "var(--primary-green)";
    if (status === "pending") return "#d97706";
    if (status === "rejected") return "#ef4444";
    return "#6b7280";
  };

  return (
    <div style={{ maxWidth: "860px", margin: "0 auto", paddingBottom: "40px" }}>
      <div style={{ marginBottom: "28px" }}>
        <h1 style={{ fontSize: "24px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Place History</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>A complete timeline of your submissions and their status updates.</p>
      </div>

      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "32px" }}>
        <div style={{ position: "relative" }}>
          {/* Vertical timeline line */}
          <div style={{ position: "absolute", left: "20px", top: "0", bottom: "0", width: "2px", backgroundColor: "var(--border-color)", zIndex: 0 }} />

          {history.map((item, i) => (
            <div key={item.id} style={{ display: "flex", gap: "20px", marginBottom: i === history.length - 1 ? 0 : "28px", position: "relative" }}>
              {/* Circle Icon */}
              <div style={{ width: "40px", height: "40px", borderRadius: "50%", backgroundColor: getBgColor(item.status), display: "flex", alignItems: "center", justifyContent: "center", flexShrink: 0, zIndex: 1, boxShadow: "0 2px 6px rgba(0,0,0,0.15)" }}>
                {getIcon(item.status)}
              </div>

              {/* Content Card */}
              <div style={{ flex: 1, backgroundColor: "#f9fafb", borderRadius: "10px", padding: "16px 20px", border: `1px solid ${item.status === "rejected" ? "#fecaca" : "var(--border-color)"}`, backgroundColor: item.status === "rejected" ? "#fef9f9" : "#f9fafb" }}>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", flexWrap: "wrap", gap: "8px", marginBottom: "6px" }}>
                  <div>
                    <span style={{ fontWeight: "700", color: "var(--text-dark)", fontSize: "15px" }}>{item.event}</span>
                    <p style={{ margin: "2px 0 0 0", fontSize: "13px", color: "var(--text-muted)" }}>
                      Place: <strong style={{ color: "var(--text-dark)" }}>{item.placeName}</strong>
                    </p>
                  </div>
                  <span style={{ fontSize: "12px", color: "var(--text-muted)", fontWeight: "600", whiteSpace: "nowrap" }}>{item.date}</span>
                </div>

                {item.remark && (
                  <div style={{ marginTop: "10px", padding: "10px 14px", backgroundColor: item.status === "rejected" ? "#fef2f2" : "#eafaf1", borderRadius: "6px", fontSize: "13px", color: item.status === "rejected" ? "#b91c1c" : "#16a34a", fontWeight: "500" }}>
                    {item.remark}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default GuidePlaceHistory;
