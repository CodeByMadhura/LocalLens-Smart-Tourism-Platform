import React, { useState } from "react";
import { Search, Eye, CheckCircle, XCircle, Filter, ChevronLeft, ChevronRight, X } from "lucide-react";
import { MOCK_PLACES } from "./data/mockData";

function StatusBadge({ status }) {
  const map = {
    PENDING: { bg: "#fffbeb", color: "#d97706", border: "#fde68a" },
    APPROVED: { bg: "#f0fdf4", color: "#16a34a", border: "#bbf7d0" },
    REJECTED: { bg: "#fef2f2", color: "#dc2626", border: "#fecaca" },
  };
  const s = map[status] || { bg: "#f3f4f6", color: "#6b7280", border: "#e5e7eb" };
  return (
    <span style={{ padding: "4px 12px", backgroundColor: s.bg, color: s.color, border: `1px solid ${s.border}`, borderRadius: "20px", fontSize: "12px", fontWeight: "700" }}>
      {status}
    </span>
  );
}

function ConfirmDialog({ visible, title, message, onConfirm, onCancel, confirmText = "Confirm", confirmColor = "#1d6d4a", children }) {
  if (!visible) return null;
  return (
    <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.5)", zIndex: 100, display: "flex", alignItems: "center", justifyContent: "center", padding: "20px" }}>
      <div style={{ backgroundColor: "white", borderRadius: "16px", padding: "32px", maxWidth: "480px", width: "100%", boxShadow: "0 20px 60px rgba(0,0,0,0.2)" }}>
        <h3 style={{ fontSize: "18px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 12px 0" }}>{title}</h3>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: "0 0 20px 0" }}>{message}</p>
        {children}
        <div style={{ display: "flex", gap: "12px", justifyContent: "flex-end", marginTop: "24px" }}>
          <button onClick={onCancel} style={{ padding: "10px 20px", borderRadius: "8px", border: "1.5px solid var(--border-color)", background: "white", fontWeight: "600", fontSize: "14px", cursor: "pointer" }}>Cancel</button>
          <button onClick={onConfirm} style={{ padding: "10px 20px", borderRadius: "8px", border: "none", background: confirmColor, color: "white", fontWeight: "700", fontSize: "14px", cursor: "pointer" }}>{confirmText}</button>
        </div>
      </div>
    </div>
  );
}

function PlaceDetailModal({ place, onClose, onApprove, onReject }) {
  if (!place) return null;
  return (
    <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.5)", zIndex: 100, display: "flex", alignItems: "center", justifyContent: "center", padding: "20px" }}>
      <div style={{ backgroundColor: "white", borderRadius: "16px", maxWidth: "580px", width: "100%", boxShadow: "0 20px 60px rgba(0,0,0,0.2)", overflow: "hidden" }}>
        <div style={{ position: "relative" }}>
          <img src={place.img} alt={place.name} style={{ width: "100%", height: "200px", objectFit: "cover" }} />
          <button onClick={onClose} style={{ position: "absolute", top: "12px", right: "12px", width: "32px", height: "32px", backgroundColor: "rgba(0,0,0,0.5)", borderRadius: "50%", border: "none", color: "white", cursor: "pointer", display: "flex", alignItems: "center", justifyContent: "center" }}>
            <X size={16} />
          </button>
        </div>
        <div style={{ padding: "24px" }}>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: "16px" }}>
            <h3 style={{ fontSize: "20px", fontWeight: "800", color: "var(--text-dark)", margin: 0 }}>{place.name}</h3>
            <StatusBadge status={place.status} />
          </div>
          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "12px", marginBottom: "16px" }}>
            {[
              { label: "Category", value: place.category },
              { label: "City", value: place.city },
              { label: "Guide", value: place.guide },
              { label: "Submitted", value: place.submitted },
            ].map(f => (
              <div key={f.label} style={{ padding: "10px", backgroundColor: "#f9fafb", borderRadius: "8px" }}>
                <p style={{ fontSize: "11px", fontWeight: "600", color: "var(--text-muted)", margin: "0 0 2px 0", textTransform: "uppercase" }}>{f.label}</p>
                <p style={{ fontSize: "14px", fontWeight: "700", color: "var(--text-dark)", margin: 0 }}>{f.value}</p>
              </div>
            ))}
          </div>
          <p style={{ fontSize: "14px", color: "var(--text-medium)", lineHeight: 1.6, marginBottom: "20px" }}>{place.description}</p>
          {place.status === "PENDING" && (
            <div style={{ display: "flex", gap: "12px" }}>
              <button onClick={() => onApprove(place)} style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center", gap: "8px", padding: "12px", backgroundColor: "#1d6d4a", color: "white", borderRadius: "8px", fontWeight: "700", border: "none", cursor: "pointer" }}>
                <CheckCircle size={18} /> Approve
              </button>
              <button onClick={() => onReject(place)} style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center", gap: "8px", padding: "12px", backgroundColor: "#fef2f2", color: "#dc2626", borderRadius: "8px", fontWeight: "700", border: "1px solid #fecaca", cursor: "pointer" }}>
                <XCircle size={18} /> Reject
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

const ITEMS_PER_PAGE = 5;

function AdminPlaceApproval() {
  const [places, setPlaces] = useState(MOCK_PLACES);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [categoryFilter, setCategoryFilter] = useState("ALL");
  const [page, setPage] = useState(1);
  const [selectedPlace, setSelectedPlace] = useState(null);
  const [confirmAction, setConfirmAction] = useState(null); // { type, place }
  const [rejectReason, setRejectReason] = useState("");
  const [toast, setToast] = useState(null);

  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3000);
  };

  const filtered = places.filter(p => {
    const matchSearch = p.name.toLowerCase().includes(search.toLowerCase()) || p.guide.toLowerCase().includes(search.toLowerCase()) || p.city.toLowerCase().includes(search.toLowerCase());
    const matchStatus = statusFilter === "ALL" || p.status === statusFilter;
    const matchCat = categoryFilter === "ALL" || p.category === categoryFilter;
    return matchSearch && matchStatus && matchCat;
  });

  const totalPages = Math.ceil(filtered.length / ITEMS_PER_PAGE);
  const paginated = filtered.slice((page - 1) * ITEMS_PER_PAGE, page * ITEMS_PER_PAGE);
  const categories = ["ALL", ...new Set(places.map(p => p.category))];

  const handleApprove = (place) => {
    setPlaces(prev => prev.map(p => p.id === place.id ? { ...p, status: "APPROVED" } : p));
    setConfirmAction(null);
    setSelectedPlace(null);
    showToast(`"${place.name}" approved successfully!`);
  };

  const handleReject = (place) => {
    setPlaces(prev => prev.map(p => p.id === place.id ? { ...p, status: "REJECTED" } : p));
    setConfirmAction(null);
    setSelectedPlace(null);
    setRejectReason("");
    showToast(`"${place.name}" rejected.`, "error");
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

      {/* Dialogs */}
      <ConfirmDialog
        visible={confirmAction?.type === "APPROVE"}
        title="Approve Place"
        message={`Are you sure you want to approve "${confirmAction?.place?.name}"? It will be visible to all users.`}
        onConfirm={() => handleApprove(confirmAction.place)}
        onCancel={() => setConfirmAction(null)}
        confirmText="Approve"
        confirmColor="#1d6d4a"
      />
      <ConfirmDialog
        visible={confirmAction?.type === "REJECT"}
        title="Reject Place"
        message={`Are you sure you want to reject "${confirmAction?.place?.name}"?`}
        onConfirm={() => handleReject(confirmAction.place)}
        onCancel={() => { setConfirmAction(null); setRejectReason(""); }}
        confirmText="Reject"
        confirmColor="#dc2626"
      >
        <textarea
          placeholder="Enter rejection reason (optional)..."
          value={rejectReason}
          onChange={e => setRejectReason(e.target.value)}
          style={{ width: "100%", padding: "10px 12px", borderRadius: "8px", border: "1.5px solid var(--border-color)", resize: "vertical", minHeight: "80px", fontFamily: "var(--font-family)", fontSize: "14px", color: "var(--text-dark)", outline: "none", boxSizing: "border-box" }}
        />
      </ConfirmDialog>
      <PlaceDetailModal
        place={selectedPlace}
        onClose={() => setSelectedPlace(null)}
        onApprove={(p) => { setSelectedPlace(null); setConfirmAction({ type: "APPROVE", place: p }); }}
        onReject={(p) => { setSelectedPlace(null); setConfirmAction({ type: "REJECT", place: p }); }}
      />

      {/* Header */}
      <div style={{ marginBottom: "24px" }}>
        <h1 style={{ fontSize: "22px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Place Approval</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Review and manage place submissions from local guides</p>
      </div>

      {/* Filters */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", padding: "18px 22px", boxShadow: "var(--card-shadow)", marginBottom: "20px", display: "flex", gap: "14px", flexWrap: "wrap", alignItems: "center" }}>
        <div style={{ flex: 1, minWidth: "200px", display: "flex", alignItems: "center", gap: "8px", backgroundColor: "#f9fafb", borderRadius: "8px", padding: "9px 14px", border: "1.5px solid var(--border-color)" }}>
          <Search size={16} color="var(--text-muted)" />
          <input value={search} onChange={e => { setSearch(e.target.value); setPage(1); }} placeholder="Search by place, guide, city..." style={{ border: "none", background: "transparent", outline: "none", fontSize: "14px", flex: 1, fontFamily: "var(--font-family)", color: "var(--text-dark)" }} />
        </div>
        <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
          <Filter size={16} color="var(--text-muted)" />
          <select value={statusFilter} onChange={e => { setStatusFilter(e.target.value); setPage(1); }} style={inputStyle}>
            <option value="ALL">All Status</option>
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
          </select>
        </div>
        <select value={categoryFilter} onChange={e => { setCategoryFilter(e.target.value); setPage(1); }} style={inputStyle}>
          {categories.map(c => <option key={c} value={c}>{c === "ALL" ? "All Categories" : c}</option>)}
        </select>
        <span style={{ fontSize: "13px", color: "var(--text-muted)", fontWeight: "600", marginLeft: "auto" }}>{filtered.length} place{filtered.length !== 1 ? "s" : ""}</span>
      </div>

      {/* Table */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", boxShadow: "var(--card-shadow)", overflow: "hidden" }}>
        <div style={{ overflowX: "auto" }}>
          <table style={{ width: "100%", borderCollapse: "collapse", minWidth: "750px" }}>
            <thead>
              <tr style={{ backgroundColor: "#f9fafb", borderBottom: "1px solid #f0f3f1" }}>
                {["Image", "Place Name", "Category", "City", "Guide", "Submitted", "Status", "Actions"].map(h => (
                  <th key={h} style={{ padding: "13px 18px", textAlign: "left", fontSize: "11px", fontWeight: "700", color: "var(--text-muted)", textTransform: "uppercase", letterSpacing: "0.5px", whiteSpace: "nowrap" }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {paginated.length === 0 ? (
                <tr><td colSpan={8} style={{ padding: "60px", textAlign: "center", color: "var(--text-muted)", fontSize: "15px" }}>No places found matching your filters</td></tr>
              ) : paginated.map((place) => (
                <tr key={place.id} style={{ borderBottom: "1px solid #f9fafb", transition: "background 0.15s" }}
                  onMouseEnter={e => e.currentTarget.style.backgroundColor = "#fafffe"}
                  onMouseLeave={e => e.currentTarget.style.backgroundColor = "transparent"}>
                  <td style={{ padding: "12px 18px" }}>
                    <img src={place.img} alt={place.name} style={{ width: "48px", height: "40px", borderRadius: "8px", objectFit: "cover" }} />
                  </td>
                  <td style={{ padding: "12px 18px", fontWeight: "700", color: "var(--text-dark)", fontSize: "14px", maxWidth: "160px" }}>
                    <span style={{ display: "block", whiteSpace: "nowrap", overflow: "hidden", textOverflow: "ellipsis" }}>{place.name}</span>
                  </td>
                  <td style={{ padding: "12px 18px" }}>
                    <span style={{ backgroundColor: "#eaf6ef", color: "#1d6d4a", padding: "3px 8px", borderRadius: "4px", fontSize: "12px", fontWeight: "700" }}>{place.category}</span>
                  </td>
                  <td style={{ padding: "12px 18px", fontSize: "13px", color: "var(--text-medium)" }}>{place.city}</td>
                  <td style={{ padding: "12px 18px", fontSize: "13px", color: "var(--text-medium)" }}>{place.guide}</td>
                  <td style={{ padding: "12px 18px", fontSize: "13px", color: "var(--text-muted)" }}>{place.submitted}</td>
                  <td style={{ padding: "12px 18px" }}><StatusBadge status={place.status} /></td>
                  <td style={{ padding: "12px 18px" }}>
                    <div style={{ display: "flex", gap: "6px" }}>
                      <button onClick={() => setSelectedPlace(place)} title="View Details" style={{ padding: "6px 10px", backgroundColor: "#eff6ff", color: "#1d4ed8", border: "none", borderRadius: "6px", cursor: "pointer", display: "flex", alignItems: "center", gap: "4px", fontSize: "12px", fontWeight: "600" }}>
                        <Eye size={14} />
                      </button>
                      {place.status === "PENDING" && (
                        <>
                          <button onClick={() => setConfirmAction({ type: "APPROVE", place })} title="Approve" style={{ padding: "6px 10px", backgroundColor: "#f0fdf4", color: "#16a34a", border: "none", borderRadius: "6px", cursor: "pointer", display: "flex", alignItems: "center", gap: "4px", fontSize: "12px", fontWeight: "600" }}>
                            <CheckCircle size={14} />
                          </button>
                          <button onClick={() => setConfirmAction({ type: "REJECT", place })} title="Reject" style={{ padding: "6px 10px", backgroundColor: "#fef2f2", color: "#dc2626", border: "none", borderRadius: "6px", cursor: "pointer", display: "flex", alignItems: "center", gap: "4px", fontSize: "12px", fontWeight: "600" }}>
                            <XCircle size={14} />
                          </button>
                        </>
                      )}
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

export default AdminPlaceApproval;
