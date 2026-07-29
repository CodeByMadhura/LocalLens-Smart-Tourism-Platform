import React from "react";
import { Heart, MapPin, Star } from "lucide-react";

function GuideSavedPlaces() {
  const saved = [
    { id: 1, title: "Summit Pass Trekking", category: "Adventure", guide: "Alex Honnold", rating: 5.0, city: "Everest Region, Nepal", img: "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=400&q=80" },
    { id: 2, title: "Trattoria Nonna's Secret", category: "Food", guide: "Giulia Rossi", rating: 4.8, city: "Rome, Italy", img: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80" },
    { id: 3, title: "Secret Sunset Overlook", category: "HiddenGems", guide: "Sam Wilson", rating: 4.9, city: "Santorini, Greece", img: "https://images.unsplash.com/photo-1472214222541-d510753a4707?auto=format&fit=crop&w=400&q=80" },
  ];

  return (
    <div style={{ maxWidth: "1000px", margin: "0 auto", paddingBottom: "40px" }}>
      <div style={{ marginBottom: "28px" }}>
        <h1 style={{ fontSize: "24px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Saved Places</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Your bookmarked destinations and tours for inspiration.</p>
      </div>

      {saved.length > 0 ? (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: "24px" }}>
          {saved.map(place => (
            <div key={place.id} style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", overflow: "hidden" }}>
              <div style={{ position: "relative", height: "175px" }}>
                <img src={place.img} alt={place.title} style={{ width: "100%", height: "100%", objectFit: "cover" }} />
                <div style={{ position: "absolute", top: "10px", right: "10px", backgroundColor: "white", padding: "6px", borderRadius: "50%", boxShadow: "0 2px 6px rgba(0,0,0,0.12)", display: "flex" }}>
                  <Heart size={18} fill="#ef4444" color="#ef4444" />
                </div>
              </div>
              <div style={{ padding: "18px" }}>
                <span style={{ display: "inline-block", padding: "3px 8px", backgroundColor: "#eaf6ef", color: "var(--primary-green)", borderRadius: "4px", fontSize: "11px", fontWeight: "700", marginBottom: "8px" }}>{place.category}</span>
                <h3 style={{ fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", margin: "0 0 6px 0" }}>{place.title}</h3>
                <p style={{ display: "flex", alignItems: "center", gap: "4px", fontSize: "12px", color: "var(--text-muted)", margin: "0 0 12px 0" }}><MapPin size={13} /> {place.city}</p>
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderTop: "1px solid #f0f3f1", paddingTop: "12px", fontSize: "13px" }}>
                  <span style={{ color: "var(--text-muted)" }}>Guide: <strong style={{ color: "var(--text-dark)" }}>{place.guide}</strong></span>
                  <span style={{ color: "#ca8a04", fontWeight: "700", display: "flex", alignItems: "center", gap: "3px" }}><Star size={13} fill="#ca8a04" color="#ca8a04" /> {place.rating}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div style={{ textAlign: "center", padding: "80px 20px", backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)" }}>
          <Heart size={48} color="#d1d5db" style={{ marginBottom: "16px" }} />
          <p style={{ fontSize: "16px", color: "var(--text-muted)" }}>No saved places yet. Explore and save places you find interesting!</p>
        </div>
      )}
    </div>
  );
}

export default GuideSavedPlaces;
