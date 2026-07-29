import React, { useState } from "react";
import { Star, Search, ThumbsUp, MessageSquare, ChevronLeft, ChevronRight } from "lucide-react";

function GuideReviews() {
  const [searchQuery, setSearchQuery] = useState("");
  const [filterRating, setFilterRating] = useState("All");
  const [currentPage, setCurrentPage] = useState(1);
  const perPage = 3;

  const ratingBreakdown = [
    { stars: 5, count: 128, pct: 82 },
    { stars: 4, count: 19, pct: 12 },
    { stars: 3, count: 6, pct: 4 },
    { stars: 2, count: 2, pct: 1 },
    { stars: 1, count: 1, pct: 1 },
  ];

  const reviews = [
    { id: 1, user: "Sarah Jenkins", avatar: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=120&q=80", rating: 5, comment: "Absolutely breathtaking experience! Alex was incredibly knowledgeable about local shrine traditions and hidden pathways.", reviewDate: "2026-07-25", visitDate: "2026-07-20", place: "Hidden Forest Shrine", helpful: 14 },
    { id: 2, user: "Michael Chen", avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=120&q=80", rating: 5, comment: "The pasta recipe we learned was sublime. This tour gave us access to authentic family kitchens not on any tourist map.", reviewDate: "2026-07-20", visitDate: "2026-07-18", place: "Trattoria Nonna's Secret", helpful: 9 },
    { id: 3, user: "Emma Thompson", avatar: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?auto=format&fit=crop&w=120&q=80", rating: 4, comment: "Beautiful scenery and great historical context. Only 4 stars because of rainy weather but our guide was well-prepared!", reviewDate: "2026-07-15", visitDate: "2026-07-12", place: "Hidden Forest Shrine", helpful: 5 },
    { id: 4, user: "David Miller", avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=120&q=80", rating: 5, comment: "Spectacular sunset view away from tourist crowds! Enjoyed local wine while watching the sun dip into the horizon.", reviewDate: "2026-07-10", visitDate: "2026-07-08", place: "Secret Sunset Overlook", helpful: 11 },
  ];

  const filtered = reviews.filter(r => {
    const q = searchQuery.toLowerCase();
    const matchSearch = r.user.toLowerCase().includes(q) || r.comment.toLowerCase().includes(q) || r.place.toLowerCase().includes(q);
    const matchRating = filterRating === "All" || r.rating === parseInt(filterRating);
    return matchSearch && matchRating;
  });

  const totalPages = Math.ceil(filtered.length / perPage);
  const displayed = filtered.slice((currentPage - 1) * perPage, currentPage * perPage);

  const renderStars = (count) => Array.from({ length: 5 }).map((_, i) => (
    <Star key={i} size={15} fill={i < count ? "#f59e0b" : "none"} color={i < count ? "#f59e0b" : "#d1d5db"} />
  ));

  return (
    <div style={{ maxWidth: "920px", margin: "0 auto", paddingBottom: "40px" }}>
      <div style={{ marginBottom: "28px" }}>
        <h1 style={{ fontSize: "24px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Ratings & Reviews</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Traveler feedback on your guided experiences.</p>
      </div>

      {/* Rating Overview */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "28px", marginBottom: "28px", display: "grid", gridTemplateColumns: "1fr 2fr", gap: "32px", alignItems: "center" }}>
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", borderRight: "1px solid var(--border-color)", paddingRight: "24px" }}>
          <p style={{ fontSize: "52px", fontWeight: "800", color: "var(--text-dark)", margin: 0, lineHeight: 1 }}>4.8</p>
          <div style={{ display: "flex", gap: "3px", margin: "10px 0 6px" }}>{renderStars(5)}</div>
          <p style={{ fontSize: "13px", color: "var(--text-muted)", fontWeight: "600", margin: 0 }}>Based on 156 reviews</p>
        </div>
        <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
          {ratingBreakdown.map(item => (
            <div key={item.stars} style={{ display: "flex", alignItems: "center", gap: "12px", fontSize: "13px" }}>
              <div style={{ width: "32px", fontWeight: "700", color: "var(--text-dark)", display: "flex", alignItems: "center", gap: "3px" }}>
                {item.stars} <Star size={12} fill="#f59e0b" color="#f59e0b" />
              </div>
              <div style={{ flex: 1, height: "8px", backgroundColor: "#f3f4f6", borderRadius: "4px", overflow: "hidden" }}>
                <div style={{ width: `${item.pct}%`, height: "100%", backgroundColor: "var(--primary-green)", borderRadius: "4px" }} />
              </div>
              <div style={{ width: "36px", textAlign: "right", color: "var(--text-muted)" }}>{item.pct}%</div>
            </div>
          ))}
        </div>
      </div>

      {/* Filters */}
      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "16px 20px", marginBottom: "24px", display: "flex", flexWrap: "wrap", gap: "12px", justifyContent: "space-between", alignItems: "center" }}>
        <div style={{ position: "relative", flex: 1, minWidth: "220px" }}>
          <Search size={17} style={{ position: "absolute", left: "12px", top: "11px", color: "var(--text-muted)" }} />
          <input type="text" placeholder="Search reviews, names, places..." value={searchQuery} onChange={e => { setSearchQuery(e.target.value); setCurrentPage(1); }} style={{ width: "100%", padding: "9px 12px 9px 36px", borderRadius: "8px", border: "1px solid var(--border-color)", outline: "none", fontSize: "14px", boxSizing: "border-box" }} />
        </div>
        <select value={filterRating} onChange={e => { setFilterRating(e.target.value); setCurrentPage(1); }} style={{ padding: "8px 12px", borderRadius: "8px", border: "1px solid var(--border-color)", fontSize: "13px", outline: "none" }}>
          <option value="All">All Ratings</option>
          {[5, 4, 3, 2, 1].map(r => <option key={r} value={r}>{r} Stars</option>)}
        </select>
      </div>

      {/* Review Cards */}
      <div style={{ display: "flex", flexDirection: "column", gap: "20px", marginBottom: "28px" }}>
        {displayed.length > 0 ? displayed.map(r => (
          <div key={r.id} style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "24px" }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", marginBottom: "14px" }}>
              <div style={{ display: "flex", gap: "12px", alignItems: "center" }}>
                <img src={r.avatar} alt={r.user} style={{ width: "46px", height: "46px", borderRadius: "50%", objectFit: "cover", flexShrink: 0 }} />
                <div>
                  <p style={{ fontWeight: "700", color: "var(--text-dark)", fontSize: "15px", margin: "0 0 2px 0" }}>{r.user}</p>
                  <p style={{ fontSize: "12px", color: "var(--text-muted)", margin: 0 }}>Reviewed: {r.reviewDate} • Visited: {r.visitDate}</p>
                </div>
              </div>
              <div style={{ display: "flex", gap: "2px" }}>{renderStars(r.rating)}</div>
            </div>
            <span style={{ display: "inline-block", padding: "3px 8px", backgroundColor: "#eaf6ef", color: "var(--primary-green)", borderRadius: "4px", fontSize: "12px", fontWeight: "700", marginBottom: "10px" }}>{r.place}</span>
            <p style={{ color: "var(--text-dark)", fontSize: "14px", lineHeight: "1.6", margin: "0 0 14px 0" }}>"{r.comment}"</p>
            <div style={{ display: "flex", gap: "16px", borderTop: "1px solid #f0f3f1", paddingTop: "12px" }}>
              <button style={{ display: "flex", alignItems: "center", gap: "6px", background: "none", border: "none", color: "var(--text-medium)", fontSize: "13px", fontWeight: "600", cursor: "pointer" }}><ThumbsUp size={15} /> Helpful ({r.helpful})</button>
              <button style={{ display: "flex", alignItems: "center", gap: "6px", background: "none", border: "none", color: "var(--text-medium)", fontSize: "13px", fontWeight: "600", cursor: "pointer" }}><MessageSquare size={15} /> Reply</button>
            </div>
          </div>
        )) : (
          <div style={{ textAlign: "center", padding: "60px", backgroundColor: "var(--white)", borderRadius: "12px" }}>
            <p style={{ color: "var(--text-muted)" }}>No reviews match your search.</p>
          </div>
        )}
      </div>

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

export default GuideReviews;
