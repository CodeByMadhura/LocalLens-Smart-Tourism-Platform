import React, { useState } from "react";
import { Search, Eye, UserX, UserCheck, Trash2, ChevronLeft, ChevronRight, Filter } from "lucide-react";
import { MOCK_TRAVELLERS } from "./data/mockData";

function StatusBadge({ status }) {
  const map = {
    ACTIVE: { bg: "#f0fdf4", color: "#16a34a", border: "#bbf7d0" },
    DISABLED: { bg: "#fef2f2", color: "#dc2626", border: "#fecaca" },
  };
  const s = map[status] || { bg: "#f3f4f6", color: "#6b7280", border: "#e5e7eb" };
  return (
    <span style={{ padding: "4px 12px", backgroundColor: s.bg, color: s.color, border: `1px solid ${s.border}`, borderRadius: "20px", fontSize: "12px", fontWeight: "700" }}>
      {status}
    </span>
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

function ProfileModal({ user, onClose }) {
  if (!user) return null;
  return (
    <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.5)", zIndex: 100, display: "flex", alignItems: "center", justifyContent: "center", padding: "20px" }}>
      <div style={{ backgroundColor: "white", borderRadius: "16px", padding: "32px", maxWidth: "440px", width: "100%", boxShadow: "0 20px 60px rgba(0,0,0,0.2)" }}>
        <div style={{ textAlign: "center", marginBottom: "24px" }}>
          <div style={{ width: "72px", height: "72px", borderRadius: "50%", backgroundColor: "#eff6ff", margin: "0 auto 12px", display: "flex", alignItems: "center", justifyContent: "center", fontSize: "28px", fontWeight: "800", color: "#1d4ed8" }}>{user.avatar}</div>
          <h3 style={{ fontSize: "20px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>{user.name}</h3>
          <StatusBadge status={user.status} />
        </div>
        {[{ label: "Email", value: user.email }, { label: "Phone", value: user.phone }, { label: "City", value: user.city }, { label: "Registered", value: user.joined }].map(f => (
          <div key={f.label} style={{ display: "flex", justifyContent: "space-between", padding: "10px 0", borderBottom: "1px solid #f0f3f1" }}>
            <span style={{ fontSize: "13px", fontWeight: "600", color: "var(--text-muted)" }}>{f.label}</span>
            <span style={{ fontSize: "13px", fontWeight: "700", color: "var(--text-dark)" }}>{f.value}</span>
          </div>
        ))}
        <button onClick={onClose} style={{ width: "100%", marginTop: "20px", padding: "12px", backgroundColor: "var(--primary-green)", color: "white", borderRadius: "8px", fontWeight: "700", border: "none", cursor: "pointer" }}>Close</button>
      </div>
    </div>
  );
}

const ITEMS_PER_PAGE = 6;

function AdminTravellers() {
  const [travellers, setTravellers] = useState(MOCK_TRAVELLERS);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [page, setPage] = useState(1);
  const [profileUser, setProfileUser] = useState(null);
  const [confirmAction, setConfirmAction] = useState(null);
  const [toast, setToast] = useState(null);

  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3000);
  };

  const filtered = travellers.filter(t => {
    const matchSearch = t.name.toLowerCase().includes(search.toLowerCase()) || t.email.toLowerCase().includes(search.toLowerCase()) || t.city.toLowerCase().includes(search.toLowerCase());
    const matchStatus = statusFilter === "ALL" || t.status === statusFilter;
    return matchSearch && matchStatus;
  });

  const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE);
  const paginated = filtered.slice((page - 1) * ITEMS_PER_PAGE, page * ITEMS_PER_PAGE);

  const toggleStatus = (user) => {
    setTravellers(prev => prev.map(t => t.id === user.id ? { ...t, status: t.status === "ACTIVE" ? "DISABLED" : "ACTIVE" } : t));
    setConfirmAction(null);
    showToast(`${user.name} has been ${user.status === "ACTIVE" ? "disabled" : "enabled"}.`);
  };

  const deleteUser = (user) => {
    setTravellers(prev => prev.filter(t => t.id !== user.id));
    setConfirmAction(null);
    showToast(`${user.name} has been removed.`, "error");
  };

  const inputStyle = { padding: "9px 12px", borderRadius: "8px", border: "1.5px solid var(--border-color)", fontSize: "14px", outline: "none", backgroundColor: "white", color: "var(--text-dark)", fontFamily: "var(--font-family)" };

  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto" }}>

      {/* Toast */}
      {toast && (
        <div style={{ position: "fixed", top: "24px", right: "24px", zIndex: 200, backgroundColor: toast.type === "error" ? "#dc2626" : "#1d6d4a", color: "white", padding: "14px 20px", borderRadius: "10px", fontWeight: "600", fontSize: "14px", boxShadow: "0 8px 24px rgba(0,0,0,0.15)", animation: "slideIn 0.3s ease" }}>
          {toast.msg}
        </div>
      )}

      <ProfileModal user={profileUser} onClose={() => setProfileUser(null)} />
      <ConfirmDialog
        visible={confirmAction?.type === "TOGGLE"}
        title={confirmAction?.user?.status === "ACTIVE" ? "Disable Traveller" : "Enable Traveller"}
        message={`Are you sure you want to ${confirmAction?.user?.status === "ACTIVE" ? "disable" : "enable"} ${confirmAction?.user?.name}?`}
        onConfirm={() => toggleStatus(confirmAction.user)}
        onCancel={() => setConfirmAction(null)}
        confirmText={confirmAction?.user?.status === "ACTIVE" ? "Disable" : "Enable"}
        confirmColor={confirmAction?.user?.status === "ACTIVE" ? "#dc2626" : "#1d6d4a"}
      />
      <ConfirmDialog
        visible={confirmAction?.type === "DELETE"}
        title="Delete Traveller"
        message={`Are you sure you want to permanently delete ${confirmAction?.user?.name}? This action cannot be undone.`}
        onConfirm={() => deleteUser(confirmAction.user)}
        onCancel={() => setConfirmAction(null)}
        confirmText="Delete"
        confirmColor="#dc2626"
      />

      <div style={{ marginBottom: "24px" }}>
        <h1 style={{ fontSize: "22px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Travellers Management</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Manage all registered travellers on the platform</p>
      </div>

      {/* Filters */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", padding: "16px 22px", boxShadow: "var(--card-shadow)", marginBottom: "20px", display: "flex", gap: "14px", flexWrap: "wrap", alignItems: "center" }}>
        <div style={{ flex: 1, minWidth: "200px", display: "flex", alignItems: "center", gap: "8px", backgroundColor: "#f9fafb", borderRadius: "8px", padding: "9px 14px", border: "1.5px solid var(--border-color)" }}>
          <Search size={16} color="var(--text-muted)" />
          <input value={search} onChange={e => { setSearch(e.target.value); setPage(1); }} placeholder="Search by name, email, city..." style={{ border: "none", background: "transparent", outline: "none", fontSize: "14px", flex: 1, fontFamily: "var(--font-family)", color: "var(--text-dark)" }} />
        </div>
        <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
          <Filter size={16} color="var(--text-muted)" />
          <select value={statusFilter} onChange={e => { setStatusFilter(e.target.value); setPage(1); }} style={inputStyle}>
            <option value="ALL">All Status</option>
            <option value="ACTIVE">Active</option>
            <option value="DISABLED">Disabled</option>
          </select>
        </div>
        <span style={{ fontSize: "13px", color: "var(--text-muted)", fontWeight: "600", marginLeft: "auto" }}>{filtered.length} traveller{filtered.length !== 1 ? "s" : ""}</span>
      </div>

      {/* Table */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", boxShadow: "var(--card-shadow)", overflow: "hidden" }}>
        <div style={{ overflowX: "auto" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", minWidth: "700px" }}>
            <thead>
              <tr style={{ backgroundColor: "#f9fafb", borderBottom: "1px solid #f0f3f1" }}>
                {["Traveller", "Email", "Phone", "City", "Joined", "Status", "Actions"].map(h => (
                  <th key={h} style={{ padding: "13px 18px", textAlign: "left", fontSize: "11px", fontWeight: "700", color: "var(--text-muted)", textTransform: "uppercase", letterSpacing: "0.5px" }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {paginated.length === 0 ? (
                <tr><td colSpan={7} style={{ padding: "60px", textAlign: "center", color: "var(--text-muted)", fontSize: "15px" }}>No travellers found</td></tr>
              ) : paginated.map(user => (
                <tr key={user.id} style={{ borderBottom: "1px solid #f9fafb" }}
                  onMouseEnter={e => e.currentTarget.style.backgroundColor = "#fafffe"}
                  onMouseLeave={e => e.currentTarget.style.backgroundColor = "transparent"}>
                  <td style={{ padding: "14px 18px" }}>
                    <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                      <div style={{ width: "38px", height: "38px", borderRadius: "50%", backgroundColor: "#eff6ff", color: "#1d4ed8", display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", fontSize: "14px", flexShrink: 0 }}>{user.avatar}</div>
                      <span style={{ fontSize: "14px", fontWeight: "700", color: "var(--text-dark)" }}>{user.name}</span>
                    </div>
                  </td>
                  <td style={{ padding: "14px 18px", fontSize: "13px", color: "var(--text-medium)" }}>{user.email}</td>
                  <td style={{ padding: "14px 18px", fontSize: "13px", color: "var(--text-medium)" }}>{user.phone}</td>
                  <td style={{ padding: "14px 18px", fontSize: "13px", color: "var(--text-medium)" }}>{user.city}</td>
                  <td style={{ padding: "14px 18px", fontSize: "13px", color: "var(--text-muted)" }}>{user.joined}</td>
                  <td style={{ padding: "14px 18px" }}><StatusBadge status={user.status} /></td>
                  <td style={{ padding: "14px 18px" }}>
                    <div style={{ display: "flex", gap: "6px" }}>
                      <button onClick={() => setProfileUser(user)} title="View Profile" style={{ padding: "6px 10px", backgroundColor: "#eff6ff", color: "#1d4ed8", border: "none", borderRadius: "6px", cursor: "pointer" }}>
                        <Eye size={14} />
                      </button>
                      <button onClick={() => setConfirmAction({ type: "TOGGLE", user })} title={user.status === "ACTIVE" ? "Disable" : "Enable"} style={{ padding: "6px 10px", backgroundColor: user.status === "ACTIVE" ? "#fff7ed" : "#f0fdf4", color: user.status === "ACTIVE" ? "#ea580c" : "#16a34a", border: "none", borderRadius: "6px", cursor: "pointer" }}>
                        {user.status === "ACTIVE" ? <UserX size={14} /> : <UserCheck size={14} />}
                      </button>
                      <button onClick={() => setConfirmAction({ type: "DELETE", user })} title="Delete" style={{ padding: "6px 10px", backgroundColor: "#fef2f2", color: "#dc2626", border: "none", borderRadius: "6px", cursor: "pointer" }}>
                        <Trash2 size={14} />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", padding: "14px 22px", borderTop: "1px solid #f0f3f1" }}>
            <span style={{ fontSize: "13px", color: "var(--text-muted)" }}>
              Showing {(page - 1) * ITEMS_PER_PAGE + 1}–{Math.min(page * ITEMS_PER_PAGE, filtered.length)} of {filtered.length}
            </span>
            <div style={{ display: "flex", gap: "8px" }}>
              <button onClick={() => setPage(p => Math.max(1, p - 1))} disabled={page === 1} style={{ padding: "7px 12px", borderRadius: "8px", border: "1.5px solid var(--border-color)", background: page === 1 ? "#f9fafb" : "white", cursor: page === 1 ? "not-allowed" : "pointer", display: "flex", alignItems: "center" }}>
                <ChevronLeft size={16} />
              </button>
              {Array.from({ length: totalPages }, (_, i) => i + 1).map(n => (
                <button key={n} onClick={() => setPage(n)} style={{ padding: "7px 13px", borderRadius: "8px", border: "1.5px solid", borderColor: page === n ? "#1d6d4a" : "var(--border-color)", backgroundColor: page === n ? "#1d6d4a" : "white", color: page === n ? "white" : "var(--text-dark)", fontWeight: "700", fontSize: "14px", cursor: "pointer" }}>
                  {n}
                </button>
              ))}
              <button onClick={() => setPage(p => Math.min(totalPages, p + 1))} disabled={page === totalPages} style={{ padding: "7px 12px", borderRadius: "8px", border: "1.5px solid var(--border-color)", background: page === totalPages ? "#f9fafb" : "white", cursor: page === totalPages ? "not-allowed" : "pointer", display: "flex", alignItems: "center" }}>
                <ChevronRight size={16} />
              </button>
            </div>
          </div>
        )}
      </div>
      <style>{`@keyframes slideIn { from { transform: translateX(100%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }`}</style>
    </div>
  );
}

export default AdminTravellers;
