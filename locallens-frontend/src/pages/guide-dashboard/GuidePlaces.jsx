import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { PlusCircle, Search, Edit2, Trash2, MapPin, Eye, Grid, List, AlertTriangle, X, ChevronLeft, ChevronRight } from "lucide-react";

function GuidePlaces() {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState("");
  const [filterCategory, setFilterCategory] = useState("All");
  const [filterStatus, setFilterStatus] = useState("All");
  const [sortBy, setSortBy] = useState("newest");
  const [viewMode, setViewMode] = useState("grid");
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);
  const perPage = 4;

  const [places, setPlaces] = useState([
    { id: 1, name: "Hidden Forest Shrine", category: "Cultural", city: "Kyoto", country: "Japan", date: "2026-07-20", status: "Approved", views: 1250, rating: 4.9, img: "https://images.unsplash.com/photo-1542044896530-05d85be9b11a?auto=format&fit=crop&w=400&q=80" },
    { id: 2, name: "Trattoria Nonna's Secret", category: "Food", city: "Rome", country: "Italy", date: "2026-07-22", status: "Approved", views: 840, rating: 4.8, img: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80" },
    { id: 3, name: "Blue Lagoon Rock Pools", category: "Nature", city: "Bali", country: "Indonesia", date: "2026-07-25", status: "Pending", views: 0, rating: 0, img: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=400&q=80" },
    { id: 4, name: "Grand Temple Ruins", category: "Historical", city: "Siem Reap", country: "Cambodia", date: "2026-07-26", status: "Rejected", views: 0, rating: 0, img: "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=400&q=80" },
    { id: 5, name: "Secret Sunset Overlook", category: "HiddenGems", city: "Santorini", country: "Greece", date: "2026-07-10", status: "Approved", views: 2100, rating: 4.9, img: "https://images.unsplash.com/photo-1472214222541-d510753a4707?auto=format&fit=crop&w=400&q=80" },
  ]);

  const filtered = places.filter(p => {
    const q = searchQuery.toLowerCase();
    const matchSearch = p.name.toLowerCase().includes(q) || p.city.toLowerCase().includes(q);
    const matchCat = filterCategory === "All" || p.category === filterCategory;
    const matchSt = filterStatus === "All" || p.status === filterStatus;
    return matchSearch && matchCat && matchSt;
  }).sort((a, b) => {
    if (sortBy === "rating") return b.rating - a.rating;
    if (sortBy === "views") return b.views - a.views;
    return new Date(b.date) - new Date(a.date);
  });

  const totalPages = Math.ceil(filtered.length / perPage);
  const displayed = filtered.slice((currentPage - 1) * perPage, currentPage * perPage);

  const confirmDelete = () => {
    if (deleteTarget) { setPlaces(p => p.filter(x => x.id !== deleteTarget.id)); setDeleteTarget(null); }
  };

  const statusBadge = (status) => {
    const map = { Approved: ["#eafaf1", "#16a34a"], Pending: ["#fffbeb", "#d97706"], Rejected: ["#fef2f2", "#dc2626"] };
    const [bg, color] = map[status] || ["#f3f4f6", "#374151"];
    return <span style={{ padding: "4px 10px", backgroundColor: bg, color, borderRadius: "20px", fontSize: "12px", fontWeight: "700" }}>{status}</span>;
  };

  const selectStyle = { padding: "8px 12px", borderRadius: "8px", border: "1px solid var(--border-color)", fontSize: "13px", color: "var(--text-dark)", backgroundColor: "var(--white)", outline: "none" };

  return (
    <div style={{ maxWidth: "1100px", margin: "0 auto", paddingBottom: "40px" }}>
      {/* Delete Confirmation Modal */}
      {deleteTarget && (
        <div style={{ position: "fixed", inset: 0, backgroundColor: "rgba(0,0,0,0.5)", display: "flex", alignItems: "center", justifyContent: "center", zIndex: 1000, padding: "20px" }}>
          <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", padding: "28px", maxWidth: "420px", width: "100%", boxShadow: "0 20px 40px rgba(0,0,0,0.2)" }}>
            <div style={{ display: "flex", alignItems: "center", gap: "12px", color: "#ef4444", marginBottom: "16px" }}>
              <div style={{ backgroundColor: "#fef2f2", padding: "10px", borderRadius: "50%" }}><AlertTriangle size={22} /></div>
              <h3 style={{ margin: 0, fontSize: "18px", fontWeight: "700" }}>Delete Place</h3>
            </div>
            <p style={{ color: "var(--text-medium)", fontSize: "14px", marginBottom: "24px" }}>
              Are you sure you want to delete <strong>"{deleteTarget.name}"</strong>? This action cannot be undone.
            </p>
            <div style={{ display: "flex", justifyContent: "flex-end", gap: "12px" }}>
              <button onClick={() => setDeleteTarget(null)} style={{ padding: "10px 18px", borderRadius: "8px", border: "1px solid var(--border-color)", backgroundColor: "var(--white)", fontWeight: "600", cursor: "pointer" }}>Cancel</button>
              <button onClick={confirmDelete} style={{ padding: "10px 18px", borderRadius: "8px", border: "none", backgroundColor: "#ef4444", color: "white", fontWeight: "700", cursor: "pointer" }}>Delete</button>
            </div>
          </div>
        </div>
      )}

      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "24px", flexWrap: "wrap", gap: "16px" }}>
        <div>
          <h1 style={{ fontSize: "24px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>My Places</h1>
          <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>View and manage all your submitted destinations.</p>
        </div>
        <button onClick={() => navigate("/guide-dashboard/add-place")} style={{ display: "flex", alignItems: "center", gap: "8px", padding: "10px 20px", backgroundColor: "var(--primary-green)", color: "white", borderRadius: "8px", fontWeight: "700", fontSize: "14px", border: "none", cursor: "pointer" }}>
          <PlusCircle size={18} /> Add New Place
        </button>
      </div>

      {/* Filters */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "20px", marginBottom: "24px", display: "flex", flexWrap: "wrap", gap: "12px", alignItems: "center" }}>
        <div style={{ position: "relative", flex: 1, minWidth: "220px" }}>
          <Search size={17} style={{ position: "absolute", left: "12px", top: "11px", color: "var(--text-muted)" }} />
          <input type="text" placeholder="Search by name or city..." value={searchQuery} onChange={e => { setSearchQuery(e.target.value); setCurrentPage(1); }} style={{ width: "100%", padding: "9px 12px 9px 36px", borderRadius: "8px", border: "1px solid var(--border-color)", outline: "none", fontSize: "14px", boxSizing: "border-box" }} />
        </div>
        <select value={filterCategory} onChange={e => { setFilterCategory(e.target.value); setCurrentPage(1); }} style={selectStyle}>
          <option value="All">All Categories</option>
          <option value="Cultural">Cultural</option>
          <option value="Food">Food</option>
          <option value="Nature">Nature</option>
          <option value="Historical">Historical</option>
          <option value="HiddenGems">Hidden Gems</option>
        </select>
        <select value={filterStatus} onChange={e => { setFilterStatus(e.target.value); setCurrentPage(1); }} style={selectStyle}>
          <option value="All">All Statuses</option>
          <option value="Approved">Approved</option>
          <option value="Pending">Pending</option>
          <option value="Rejected">Rejected</option>
        </select>
        <select value={sortBy} onChange={e => setSortBy(e.target.value)} style={selectStyle}>
          <option value="newest">Newest First</option>
          <option value="rating">Highest Rating</option>
          <option value="views">Most Viewed</option>
        </select>
        <div style={{ display: "flex", border: "1px solid var(--border-color)", borderRadius: "8px", overflow: "hidden" }}>
          <button onClick={() => setViewMode("grid")} style={{ padding: "8px 12px", border: "none", backgroundColor: viewMode === "grid" ? "#eaf6ef" : "var(--white)", color: viewMode === "grid" ? "var(--primary-green)" : "var(--text-medium)", cursor: "pointer" }}><Grid size={16} /></button>
          <button onClick={() => setViewMode("table")} style={{ padding: "8px 12px", border: "none", backgroundColor: viewMode === "table" ? "#eaf6ef" : "var(--white)", color: viewMode === "table" ? "var(--primary-green)" : "var(--text-medium)", cursor: "pointer" }}><List size={16} /></button>
        </div>
      </div>

      {/* Grid View */}
      {viewMode === "grid" ? (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: "24px", marginBottom: "28px" }}>
          {displayed.length > 0 ? displayed.map(place => (
            <div key={place.id} style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", overflow: "hidden", display: "flex", flexDirection: "column" }}>
              <div style={{ position: "relative", height: "175px" }}>
                <img src={place.img} alt={place.name} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
                <div style={{ position: "absolute", top: "10px", right: "10px" }}>{statusBadge(place.status)}</div>
                <div style={{ position: "absolute", bottom: "10px", left: "10px", backgroundColor: "rgba(0,0,0,0.65)", color: "white", padding: "2px 8px", borderRadius: "4px", fontSize: "11px", fontWeight: "700" }}>{place.category}</div>
              </div>
              <div style={{ padding: "18px", flex: 1, display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
                <div>
                  <h3 style={{ fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", margin: "0 0 6px 0" }}>{place.name}</h3>
                  <div style={{ display: "flex", alignItems: "center", gap: "4px", fontSize: "13px", color: "var(--text-muted)", marginBottom: "14px" }}><MapPin size={13} /> {place.city}, {place.country}</div>
                </div>
                <div>
                  <div style={{ display: "flex", justifyContent: "space-between", fontSize: "12px", borderTop: "1px solid #f0f3f1", paddingTop: "10px", marginBottom: "12px", color: "var(--text-muted)" }}>
                    <span>{place.views} views</span>
                    <span style={{ color: "#ca8a04", fontWeight: "700" }}>{place.rating > 0 ? `★ ${place.rating}` : "New"}</span>
                  </div>
                  <div style={{ display: "flex", gap: "8px" }}>
                    <button style={{ flex: 1, padding: "7px", backgroundColor: "#ebf5fb", color: "#3498db", border: "none", borderRadius: "6px", fontSize: "12px", fontWeight: "700", cursor: "pointer", display: "flex", alignItems: "center", justifyContent: "center", gap: "4px" }}><Eye size={14} /> View</button>
                    <button style={{ flex: 1, padding: "7px", backgroundColor: "#fef9e7", color: "#f39c12", border: "none", borderRadius: "6px", fontSize: "12px", fontWeight: "700", cursor: "pointer", display: "flex", alignItems: "center", justifyContent: "center", gap: "4px" }}><Edit2 size={14} /> Edit</button>
                    <button onClick={() => setDeleteTarget(place)} style={{ padding: "7px 10px", backgroundColor: "#fef2f2", color: "#ef4444", border: "none", borderRadius: "6px", cursor: "pointer" }}><Trash2 size={14} /></button>
                  </div>
                </div>
              </div>
            </div>
          )) : (
            <div style={{ gridColumn: "1/-1", textAlign: "center", padding: "60px 20px", backgroundColor: "var(--white)", borderRadius: "12px" }}>
              <p style={{ color: "var(--text-muted)", fontSize: "15px" }}>No places match your search.</p>
            </div>
          )}
        </div>
      ) : (
        <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", overflow: "hidden", marginBottom: "28px" }}>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f9fafb" }}>
                {["Place", "Category", "Location", "Status", "Actions"].map(h => (
                  <th key={h} style={{ padding: "14px 20px", textAlign: "left", fontSize: "12px", fontWeight: "600", color: "var(--text-muted)", textTransform: "uppercase" }}>{h}</th>
                ))}
              </tr>
            </thead>
            <tbody>
              {displayed.map(place => (
                <tr key={place.id} style={{ borderTop: "1px solid #f0f3f1" }}>
                  <td style={{ padding: "14px 20px", fontWeight: "700", color: "var(--text-dark)", fontSize: "14px" }}>{place.name}</td>
                  <td style={{ padding: "14px 20px", fontSize: "13px", color: "var(--text-medium)" }}>{place.category}</td>
                  <td style={{ padding: "14px 20px", fontSize: "13px", color: "var(--text-muted)" }}>{place.city}</td>
                  <td style={{ padding: "14px 20px" }}>{statusBadge(place.status)}</td>
                  <td style={{ padding: "14px 20px" }}>
                    <div style={{ display: "flex", gap: "8px" }}>
                      <button style={{ padding: "6px 10px", backgroundColor: "#ebf5fb", color: "#3498db", border: "none", borderRadius: "6px", cursor: "pointer" }}><Eye size={15} /></button>
                      <button style={{ padding: "6px 10px", backgroundColor: "#fef9e7", color: "#f39c12", border: "none", borderRadius: "6px", cursor: "pointer" }}><Edit2 size={15} /></button>
                      <button onClick={() => setDeleteTarget(place)} style={{ padding: "6px 10px", backgroundColor: "#fef2f2", color: "#ef4444", border: "none", borderRadius: "6px", cursor: "pointer" }}><Trash2 size={15} /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Pagination */}
      {totalPages > 1 && (
        <div style={{ display: "flex", justifyContent: "center", alignItems: "center", gap: "12px" }}>
          <button disabled={currentPage === 1} onClick={() => setCurrentPage(p => p - 1)} style={{ padding: "8px 12px", borderRadius: "8px", border: "1px solid var(--border-color)", backgroundColor: "var(--white)", opacity: currentPage === 1 ? 0.5 : 1, cursor: currentPage === 1 ? "not-allowed" : "pointer" }}><ChevronLeft size={18} /></button>
          <span style={{ fontSize: "14px", fontWeight: "600", color: "var(--text-medium)" }}>Page {currentPage} of {totalPages}</span>
          <button disabled={currentPage === totalPages} onClick={() => setCurrentPage(p => p + 1)} style={{ padding: "8px 12px", borderRadius: "8px", border: "1px solid var(--border-color)", backgroundColor: "var(--white)", opacity: currentPage === totalPages ? 0.5 : 1, cursor: currentPage === totalPages ? "not-allowed" : "pointer" }}><ChevronRight size={18} /></button>
        </div>
      )}
    </div>
  );
}

export default GuidePlaces;
