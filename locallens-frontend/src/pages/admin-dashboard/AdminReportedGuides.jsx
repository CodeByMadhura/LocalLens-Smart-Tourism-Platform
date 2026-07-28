import React, { useState } from "react";
import { Eye, UserX, Trash2, Check, AlertTriangle } from "lucide-react";
import { MOCK_REPORTED_GUIDES } from "./data/mockData";

function StatusBadge({ status }) {
  const map = {
    UNDER_REVIEW: { bg: "#fffbeb", color: "#d97706", border: "#fde68a" },
    SUSPENDED: { bg: "#fff7ed", color: "#ea580c", border: "#fed7aa" },
    IGNORED: { bg: "#f3f4f6", color: "#6b7280", border: "#e5e7eb" },
  };
  const s = map[status] || { bg: "#f3f4f6", color: "#6b7280", border: "#e5e7eb" };
  return (
    <span style={{ padding: "4px 12px", backgroundColor: s.bg, color: s.color, border: `1px solid ${s.border}`, borderRadius: "20px", fontSize: "12px", fontWeight: "700" }}>
      {status.replace("_", " ")}
    </span>
  );
}

function ActionBtn({ icon, label, onClick, color, bg }) {
  return (
    <button
      onClick={onClick}
      title={label}
      style={{ display: "flex", alignItems: "center", gap: "5px", padding: "7px 12px", backgroundColor: bg, color, border: "none", borderRadius: "7px", cursor: "pointer", fontSize: "12px", fontWeight: "700", transition: "opacity 0.15s" }}
      onMouseEnter={e => { e.currentTarget.style.opacity = "0.8"; }}
      onMouseLeave={e => { e.currentTarget.style.opacity = "1"; }}
    >
      {icon} {label}
    </button>
  );
}

function ConfirmDialog({ visible, title, message, onConfirm, onCancel, confirmText, confirmColor = "#1d6d4a" }) {
  if (!visible) return null;
  return (
    <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.5)", zIndex: 100, display: "flex", alignItems: "center", justifyContent: "center", padding: "20px" }}>
      <div style={{ backgroundColor: "white", borderRadius: "16px", padding: "32px", maxWidth: "440px", width: "100%", boxShadow: "0 20px 60px rgba(0,0,0,0.2)" }}>
        <h3 style={{ fontSize: "18px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 12px 0" }}>{title}</h3>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: "0 0 24px 0" }}>{message}</p>
        <div style={{ display: "flex", gap: "12px", justifyContent: "flex-end" }}>
          <button onClick={onCancel} style={{ padding: "10px 20px", borderRadius: "8px", border: "1.5px solid var(--border-color)", background: "white", fontWeight: "600", fontSize: "14px", cursor: "pointer" }}>Cancel</button>
          <button onClick={onConfirm} style={{ padding: "10px 20px", borderRadius: "8px", border: "none", background: confirmColor, color: "white", fontWeight: "700", fontSize: "14px", cursor: "pointer" }}>{confirmText}</button>
        </div>
      </div>
    </div>
  );
}

function AdminReportedGuides() {
  const [reports, setReports] = useState(MOCK_REPORTED_GUIDES);
  const [confirmAction, setConfirmAction] = useState(null);
  const [toast, setToast] = useState(null);

  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3000);
  };

  const handleAction = (id, newStatus, label) => {
    setReports(prev => prev.map(r => r.id === id ? { ...r, status: newStatus } : r));
    setConfirmAction(null);
    showToast(`Action taken: ${label}`);
  };

  const handleRemove = (id) => {
    setReports(prev => prev.filter(r => r.id !== id));
    setConfirmAction(null);
    showToast("Report removed from the list.", "error");
  };

  const totalReports = reports.reduce((sum, r) => sum + r.reports, 0);

  return (
    <div style={{ maxWidth: "1000px", margin: "0 auto" }}>
      {toast && (
        <div style={{ position: "fixed", top: "24px", right: "24px", zIndex: 200, backgroundColor: toast.type === "error" ? "#dc2626" : "#1d6d4a", color: "white", padding: "14px 20px", borderRadius: "10px", fontWeight: "600", fontSize: "14px", boxShadow: "0 8px 24px rgba(0,0,0,0.15)", animation: "slideIn 0.3s ease" }}>
          {toast.msg}
        </div>
      )}

      <ConfirmDialog
        visible={!!confirmAction}
        title={confirmAction?.title || ""}
        message={confirmAction?.message || ""}
        onConfirm={confirmAction?.onConfirm}
        onCancel={() => setConfirmAction(null)}
        confirmText={confirmAction?.confirmText || "Confirm"}
        confirmColor={confirmAction?.color || "#1d6d4a"}
      />

      <div style={{ marginBottom: "24px" }}>
        <h1 style={{ fontSize: "22px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Reported Guides</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Review and take action on reported local guides</p>
      </div>

      {/* Summary Bar */}
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(170px, 1fr))", gap: "16px", marginBottom: "24px" }}>
        {[
          { label: "Total Reports", value: reports.length, color: "#dc2626", bg: "#fef2f2" },
          { label: "Under Review", value: reports.filter(r => r.status === "UNDER_REVIEW").length, color: "#d97706", bg: "#fffbeb" },
          { label: "Suspended", value: reports.filter(r => r.status === "SUSPENDED").length, color: "#ea580c", bg: "#fff7ed" },
          { label: "Total Reports Filed", value: totalReports, color: "#7c3aed", bg: "#f5f3ff" },
        ].map(card => (
          <div key={card.label} style={{ backgroundColor: "var(--white)", borderRadius: "12px", padding: "18px", boxShadow: "var(--card-shadow)", border: "1px solid #f0f3f1" }}>
            <p style={{ fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", margin: "0 0 6px 0", textTransform: "uppercase", letterSpacing: "0.5px" }}>{card.label}</p>
            <p style={{ fontSize: "26px", fontWeight: "800", color: card.color, margin: 0 }}>{card.value}</p>
          </div>
        ))}
      </div>

      {/* Report Cards */}
      {reports.length === 0 ? (
        <div style={{ textAlign: "center", padding: "80px 20px", backgroundColor: "var(--white)", borderRadius: "14px", boxShadow: "var(--card-shadow)" }}>
          <AlertTriangle size={48} color="#d1d5db" style={{ marginBottom: "16px" }} />
          <p style={{ fontSize: "16px", color: "var(--text-muted)" }}>No reported guides at the moment.</p>
        </div>
      ) : (
        <div style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
          {reports.map(report => (
            <div key={report.id} style={{ backgroundColor: "var(--white)", borderRadius: "14px", padding: "22px", boxShadow: "var(--card-shadow)", border: "1px solid #f0f3f1", transition: "box-shadow 0.2s ease" }}>
              <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", flexWrap: "wrap", gap: "12px" }}>
                <div style={{ display: "flex", alignItems: "center", gap: "14px" }}>
                  <div style={{ width: "52px", height: "52px", borderRadius: "50%", background: "linear-gradient(135deg, #fef2f2, #fee2e2)", color: "#dc2626", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", fontSize: "20px", flexShrink: 0 }}>
                    {report.avatar}
                  </div>
                  <div>
                    <h3 style={{ fontSize: "16px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 2px 0" }}>{report.guide}</h3>
                    <p style={{ fontSize: "12px", color: "var(--text-muted)", margin: 0 }}>{report.guideEmail}</p>
                  </div>
                </div>
                <StatusBadge status={report.status} />
              </div>

              <div style={{ marginTop: "16px", padding: "14px 16px", backgroundColor: "#fef9f2", borderRadius: "10px", borderLeft: "4px solid #d97706" }}>
                <p style={{ fontSize: "13px", fontWeight: "700", color: "#92400e", margin: "0 0 4px 0" }}>Reason for Report:</p>
                <p style={{ fontSize: "14px", color: "var(--text-dark)", margin: 0 }}>{report.reason}</p>
              </div>

              <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: "12px", marginTop: "14px" }}>
                <div style={{ padding: "10px 12px", backgroundColor: "#f9fafb", borderRadius: "8px" }}>
                  <p style={{ fontSize: "11px", fontWeight: "600", color: "var(--text-muted)", margin: "0 0 2px 0", textTransform: "uppercase" }}>Reports Count</p>
                  <p style={{ fontSize: "18px", fontWeight: "800", color: "#dc2626", margin: 0 }}>{report.reports}</p>
                </div>
                <div style={{ padding: "10px 12px", backgroundColor: "#f9fafb", borderRadius: "8px" }}>
                  <p style={{ fontSize: "11px", fontWeight: "600", color: "var(--text-muted)", margin: "0 0 2px 0", textTransform: "uppercase" }}>Reported By</p>
                  <p style={{ fontSize: "13px", fontWeight: "600", color: "var(--text-dark)", margin: 0, whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>{report.reportedBy}</p>
                </div>
                <div style={{ padding: "10px 12px", backgroundColor: "#f9fafb", borderRadius: "8px" }}>
                  <p style={{ fontSize: "11px", fontWeight: "600", color: "var(--text-muted)", margin: "0 0 2px 0", textTransform: "uppercase" }}>Date</p>
                  <p style={{ fontSize: "13px", fontWeight: "600", color: "var(--text-dark)", margin: 0 }}>{report.date}</p>
                </div>
              </div>

              <div style={{ display: "flex", gap: "10px", marginTop: "16px", flexWrap: "wrap" }}>
                <ActionBtn
                  icon={<UserX size={14} />}
                  label="Suspend Guide"
                  color="#ea580c"
                  bg="#fff7ed"
                  onClick={() => setConfirmAction({ title: "Suspend Guide", message: `Suspend ${report.guide} from the platform?`, confirmText: "Suspend", color: "#ea580c", onConfirm: () => handleAction(report.id, "SUSPENDED", "Guide suspended") })}
                />
                <ActionBtn
                  icon={<Trash2 size={14} />}
                  label="Remove Guide"
                  color="#dc2626"
                  bg="#fef2f2"
                  onClick={() => setConfirmAction({ title: "Remove Guide", message: `Permanently remove ${report.guide}?`, confirmText: "Remove", color: "#dc2626", onConfirm: () => handleRemove(report.id) })}
                />
                <ActionBtn
                  icon={<Check size={14} />}
                  label="Ignore Report"
                  color="#6b7280"
                  bg="#f3f4f6"
                  onClick={() => setConfirmAction({ title: "Ignore Report", message: `Mark this report for ${report.guide} as ignored?`, confirmText: "Ignore", color: "#6b7280", onConfirm: () => handleAction(report.id, "IGNORED", "Report ignored") })}
                />
                <ActionBtn
                  icon={<Eye size={14} />}
                  label="Mark Under Review"
                  color="#1d4ed8"
                  bg="#eff6ff"
                  onClick={() => handleAction(report.id, "UNDER_REVIEW", "Marked as under review")}
                />
              </div>
            </div>
          ))}
        </div>
      )}

      <style>{`@keyframes slideIn { from { transform: translateX(100%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }`}</style>
    </div>
  );
}

export default AdminReportedGuides;
