import React, { useEffect } from "react";
import { Routes, Route, Navigate, useLocation } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Discover from "./pages/Discover";
import Login from "./pages/Login";
import Register from "./pages/Register";

// Guide Dashboard pages
import GuideDashboardLayout from "./pages/guide-dashboard/GuideDashboardLayout";
import GuideDashboardHome from "./pages/guide-dashboard/GuideDashboardHome";
import GuideProfile from "./pages/guide-dashboard/GuideProfile";
import GuidePlaces from "./pages/guide-dashboard/GuidePlaces";
import GuideAddPlace from "./pages/guide-dashboard/GuideAddPlace";
import GuideReviews from "./pages/guide-dashboard/GuideReviews";
import GuideSavedPlaces from "./pages/guide-dashboard/GuideSavedPlaces";

// Admin Dashboard pages
import AdminDashboardLayout from "./pages/admin-dashboard/AdminDashboardLayout";
import AdminDashboardHome from "./pages/admin-dashboard/AdminDashboardHome";
import AdminPlaceApproval from "./pages/admin-dashboard/AdminPlaceApproval";
import AdminTravellers from "./pages/admin-dashboard/AdminTravellers";
import AdminLocalGuides from "./pages/admin-dashboard/AdminLocalGuides";
import AdminReportedGuides from "./pages/admin-dashboard/AdminReportedGuides";
import AdminAnalytics from "./pages/admin-dashboard/AdminAnalytics";
import AdminActiveUsers from "./pages/admin-dashboard/AdminActiveUsers";

// ProtectedGuideRoute: only LOCAL_GUIDE users can access the dashboard
function ProtectedGuideRoute({ children }) {
  const { isLoggedIn, isLocalGuide, loading } = useAuth();

  if (loading) {
    // Show a simple loading screen while checking auth state
    return (
      <div style={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "100vh", backgroundColor: "var(--light-green-bg)" }}>
        <div style={{ textAlign: "center" }}>
          <div style={{ width: "40px", height: "40px", border: "3px solid var(--primary-green)", borderTop: "3px solid transparent", borderRadius: "50%", animation: "spin 1s linear infinite", margin: "0 auto 16px" }}></div>
          <p style={{ color: "var(--text-muted)", fontSize: "14px" }}>Loading...</p>
        </div>
      </div>
    );
  }

  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }

  if (!isLocalGuide) {
    // Travellers can't access guide dashboard
    return <Navigate to="/" replace />;
  }

  return children;
}

// ProtectedAdminRoute: only ADMIN users can access the admin dashboard
function ProtectedAdminRoute({ children }) {
  const { isLoggedIn, isAdmin, loading } = useAuth();

  if (loading) {
    return (
      <div style={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "100vh", backgroundColor: "var(--light-green-bg)" }}>
        <div style={{ textAlign: "center" }}>
          <div style={{ width: "40px", height: "40px", border: "3px solid var(--primary-green)", borderTop: "3px solid transparent", borderRadius: "50%", animation: "spin 1s linear infinite", margin: "0 auto 16px" }}></div>
          <p style={{ color: "var(--text-muted)", fontSize: "14px" }}>Loading...</p>
        </div>
      </div>
    );
  }

  if (!isLoggedIn) {
    return <Navigate to="/login" replace />;
  }

  if (!isAdmin) {
    return <Navigate to="/" replace />;
  }

  return children;
}

// Main app content that has access to router hooks
function AppContent() {
  const location = useLocation();
  const isDashboard = location.pathname.startsWith("/guide-dashboard") || location.pathname.startsWith("/admin-dashboard");

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  // Mock places data for the Places page
  const mockPlaces = [
    { id: 1, title: "Hidden Forest Shrine", category: "Cultural", guide: "Kenji Sato", rating: 4.9, img: "https://images.unsplash.com/photo-1542044896530-05d85be9b11a?auto=format&fit=crop&w=400&q=80" },
    { id: 2, title: "Trattoria Nonna's Secret", category: "Food", guide: "Giulia Rossi", rating: 4.8, img: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=400&q=80" },
    { id: 3, title: "Summit Pass Trekking", category: "Adventure", guide: "Alex Honnold", rating: 5.0, img: "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=400&q=80" },
    { id: 4, title: "Blue Lagoon Rock Pools", category: "Nature", guide: "Kai Olsen", rating: 4.7, img: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=400&q=80" },
    { id: 5, title: "Grand Temple Ruins", category: "Historical", guide: "Ananya Iyer", rating: 4.9, img: "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=400&q=80" },
    { id: 6, title: "Secret Sunset Overlook", category: "HiddenGems", guide: "Sam Wilson", rating: 4.9, img: "https://images.unsplash.com/photo-1472214222541-d510753a4707?auto=format&fit=crop&w=400&q=80" },
  ];

  const pageStyle = {
    padding: "60px 8%",
    minHeight: "calc(100vh - 160px)",
    backgroundColor: "var(--light-green-bg)",
  };

  const inputStyle = {
    width: "100%",
    padding: "12px",
    borderRadius: "8px",
    border: "1.5px solid var(--border-color)",
    marginBottom: "20px",
    outline: "none",
    boxSizing: "border-box",
  };

  return (
    <>
      {/* Hide the main header/footer for the guide dashboard (it has its own layout) */}
      {!isDashboard && <Header />}

      <main>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Discover />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/places" element={
            <div style={pageStyle}>
              <h2 style={{ fontSize: "36px", fontWeight: "800", color: "#0f172a", marginBottom: "8px" }}>Explore Authentic Places</h2>
              <p style={{ fontSize: "16px", color: "var(--text-muted)", marginBottom: "40px" }}>Handpicked local tours, activities, and hidden spots</p>
              <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))", gap: "32px" }}>
                {mockPlaces.map((place) => (
                  <div key={place.id} style={{ backgroundColor: "var(--white)", borderRadius: "12px", overflow: "hidden", boxShadow: "var(--card-shadow)" }}>
                    <img src={place.img} alt={place.title} style={{ width: "100%", height: "180px", objectFit: "cover" }} />
                    <div style={{ padding: "20px" }}>
                      <span style={{ display: "inline-block", backgroundColor: "#eaf6ef", color: "var(--primary-green)", padding: "4px 8px", borderRadius: "4px", fontSize: "12px", fontWeight: "700", marginBottom: "12px" }}>{place.category}</span>
                      <h3 style={{ fontSize: "18px", fontWeight: "700", marginBottom: "8px", color: "#0f172a" }}>{place.title}</h3>
                      <div style={{ display: "flex", justifyContent: "space-between", fontSize: "13px", color: "var(--text-muted)", borderTop: "1px solid #f0f3f1", paddingTop: "12px", marginTop: "12px" }}>
                        <span>Guide: <strong>{place.guide}</strong></span>
                        <span style={{ color: "#f39c12", fontWeight: "bold" }}>★ {place.rating}</span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          } />

          <Route path="/about" element={
            <div style={{ ...pageStyle, maxWidth: "860px", margin: "0 auto" }}>
              <h2 style={{ fontSize: "36px", fontWeight: "800", color: "#0f172a", marginBottom: "8px" }}>About LocalLens</h2>
              <p style={{ fontSize: "16px", color: "var(--text-muted)", marginBottom: "40px" }}>Bridging the gap between travellers and local guide expertise</p>
              <div style={{ backgroundColor: "var(--white)", padding: "40px", borderRadius: "12px", boxShadow: "var(--card-shadow)", lineHeight: "1.8", color: "var(--text-medium)" }}>
                <p style={{ marginBottom: "20px" }}>LocalLens is a smart tourism platform that connects visitors with passionate local guides. We believe the best way to experience any destination is through the eyes of the people who call it home.</p>
                <p style={{ marginBottom: "20px" }}>Whether you're looking to explore a secret trailhead, taste authentic traditional dishes, or learn the historic tales behind ancient temples, our community of certified guides is here to show you the real side of the places you visit.</p>
                <p>Join our community today to support local tourism economies and embark on unforgettable journeys.</p>
              </div>
            </div>
          } />

          <Route path="/contact" element={
            <div style={pageStyle}>
              <h2 style={{ fontSize: "36px", fontWeight: "800", color: "#0f172a", marginBottom: "8px", textAlign: "center" }}>Get In Touch</h2>
              <p style={{ fontSize: "16px", color: "var(--text-muted)", marginBottom: "40px", textAlign: "center" }}>Have questions? Send us a message and we'll reply within 24 hours.</p>
              <form style={{ backgroundColor: "var(--white)", padding: "40px", borderRadius: "12px", boxShadow: "var(--card-shadow)", maxWidth: "600px", margin: "0 auto" }} onSubmit={(e) => { e.preventDefault(); alert("Message sent!"); }}>
                <label style={{ display: "block", marginBottom: "8px", fontWeight: "600" }}>Your Name</label>
                <input type="text" placeholder="John Doe" style={inputStyle} required />
                <label style={{ display: "block", marginBottom: "8px", fontWeight: "600" }}>Email Address</label>
                <input type="email" placeholder="john@example.com" style={inputStyle} required />
                <label style={{ display: "block", marginBottom: "8px", fontWeight: "600" }}>Message</label>
                <textarea placeholder="Write your message..." style={{ ...inputStyle, height: "120px", fontFamily: "inherit", resize: "vertical" }} required></textarea>
                <button type="submit" style={{ width: "100%", padding: "14px", backgroundColor: "var(--primary-green)", color: "var(--white)", borderRadius: "8px", fontSize: "16px", fontWeight: "600", border: "none", cursor: "pointer" }}>Send Message</button>
              </form>
            </div>
          } />

          {/* Protected Guide Dashboard Routes */}
          <Route path="/guide-dashboard" element={
            <ProtectedGuideRoute>
              <GuideDashboardLayout />
            </ProtectedGuideRoute>
          }>
            <Route index element={<GuideDashboardHome />} />
            <Route path="profile" element={<GuideProfile />} />
            <Route path="places" element={<GuidePlaces />} />
            <Route path="add-place" element={<GuideAddPlace />} />
            <Route path="reviews" element={<GuideReviews />} />
            <Route path="saved" element={<GuideSavedPlaces />} />
          </Route>

          {/* Protected Admin Dashboard Routes */}
          <Route path="/admin-dashboard" element={
            <ProtectedAdminRoute>
              <AdminDashboardLayout />
            </ProtectedAdminRoute>
          }>
            <Route index element={<AdminDashboardHome />} />
            <Route path="place-approval" element={<AdminPlaceApproval />} />
            <Route path="travellers" element={<AdminTravellers />} />
            <Route path="local-guides" element={<AdminLocalGuides />} />
            <Route path="reported-guides" element={<AdminReportedGuides />} />
            <Route path="analytics" element={<AdminAnalytics />} />
            <Route path="active-users" element={<AdminActiveUsers />} />
          </Route>

          {/* 404 Fallback */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>

      {!isDashboard && <Footer />}

      {/* Global spinner animation */}
      <style>{`
        @keyframes spin {
          from { transform: rotate(0deg); }
          to { transform: rotate(360deg); }
        }
      `}</style>
    </>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;