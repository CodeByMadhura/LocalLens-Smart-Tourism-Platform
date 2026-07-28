import React, { useState } from "react";
import heroImg from "../assets/hero.png";
import { useNavigate } from "react-router-dom";

function Discover() {
  const navigate = useNavigate();
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [searchQuery, setSearchQuery] = useState("");

  // SVG Category Icon Components
  const CompassIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10" />
      <polygon points="16.24 7.76 14.12 14.12 7.76 16.24 9.88 9.88 16.24 7.76" />
    </svg>
  );

  const UtensilsIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M3 2v7c0 1.1.9 2 2 2h4a2 2 0 0 0 2-2V2" />
      <path d="M7 2v20" />
      <path d="M21 15V2a5 5 0 0 0-5 5v8c0 1.1.9 2 2 2h3Z" />
      <path d="M18 17v5" />
    </svg>
  );

  const DumbbellIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M6.5 6.5 4 9H2V15H4L6.5 17.5V6.5Z" />
      <path d="M17.5 6.5 20 9H22V15H20L17.5 17.5V6.5Z" />
      <path d="M6.5 12H17.5" />
    </svg>
  );

  const MuseumIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4 22V10" />
      <path d="M20 22V10" />
      <path d="M12 22V10" />
      <path d="M2 22H22" />
      <path d="M3 10h18" />
      <path d="M12 2 2 10h20L12 2Z" />
    </svg>
  );

  const TreeIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="m12 19 7-7H5l7 7Z" />
      <path d="m12 13 6-6H6l6 6Z" />
      <path d="m12 7 4-4H8l4 4Z" />
      <path d="M12 19v3" />
    </svg>
  );

  const CameraIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z" />
      <circle cx="12" cy="13" r="4" />
    </svg>
  );

  const BookIcon = () => (
    <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
      <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
    </svg>
  );

  const categories = [
    { name: "All", icon: <CompassIcon /> },
    { name: "Food", icon: <UtensilsIcon /> },
    { name: "Adventure", icon: <DumbbellIcon /> },
    { name: "Cultural", icon: <MuseumIcon /> },
    { name: "Nature", icon: <TreeIcon /> },
    { name: "HiddenGems", icon: <CameraIcon /> },
    { name: "Historical", icon: <BookIcon /> },
  ];

  // Icons for sections and buttons
  const DiamondIcon = () => (
    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ marginRight: "6px" }}>
      <path d="M12 2L2 12L12 22L22 12L12 2Z" fill="currentColor" />
    </svg>
  );

  const ExploreIcon = () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ marginRight: "6px" }}>
      <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.59 20 4 16.41 4 12C4 7.59 7.59 4 12 4C16.41 4 20 7.59 20 12C20 16.41 16.41 20 12 20ZM12 10.5C11.17 10.5 10.5 11.17 10.5 12C10.5 12.83 11.17 13.5 12 13.5C12.83 13.5 13.5 12.83 13.5 12C13.5 11.17 12.83 10.5 12 10.5ZM12 6C8.69 6 6 8.69 6 12C6 15.31 8.69 18 12 18C15.31 18 18 15.31 18 12C18 8.69 15.31 6 12 6Z" fill="currentColor" />
    </svg>
  );

  const MapPinIcon = () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ marginRight: "6px" }}>
      <path d="M12 2C8.13 2 5 5.13 5 9C5 14.25 12 22 12 22C12 22 19 14.25 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="currentColor" />
    </svg>
  );

  // Layout Styles
  const heroStyle = {
    position: "relative",
    height: "560px",
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    backgroundImage: `linear-gradient(rgba(12, 45, 28, 0.65), rgba(12, 45, 28, 0.65)), url(${heroImg || ""})`,
    backgroundSize: "cover",
    backgroundPosition: "center",
    color: "var(--white)",
    textAlign: "center",
    padding: "80px 24px 0 24px",
  };

  const badgeStyle = {
    display: "flex",
    alignItems: "center",
    padding: "6px 16px",
    borderRadius: "20px",
    border: "1px solid rgba(255, 255, 255, 0.3)",
    backgroundColor: "rgba(255, 255, 255, 0.1)",
    fontSize: "13px",
    fontWeight: "600",
    marginBottom: "24px",
    textTransform: "uppercase",
    letterSpacing: "0.5px",
  };

  const headingStyle = {
    fontSize: "48px",
    fontWeight: "800",
    marginBottom: "16px",
    maxWidth: "800px",
    lineHeight: "1.2",
  };

  const subtitleStyle = {
    fontSize: "18px",
    fontWeight: "400",
    marginBottom: "40px",
    maxWidth: "650px",
    opacity: "0.9",
  };

  const searchContainerStyle = {
    display: "flex",
    width: "100%",
    maxWidth: "600px",
    backgroundColor: "var(--white)",
    borderRadius: "30px",
    padding: "6px",
    boxShadow: "0 8px 30px rgba(0, 0, 0, 0.15)",
    overflow: "hidden",
  };

  const searchInputStyle = {
    flex: 1,
    border: "none",
    outline: "none",
    paddingLeft: "24px",
    fontSize: "15px",
    color: "var(--text-dark)",
  };

  const exploreBtnStyle = {
    display: "flex",
    alignItems: "center",
    backgroundColor: "var(--primary-green)",
    color: "var(--white)",
    padding: "12px 28px",
    borderRadius: "24px",
    fontSize: "14px",
    fontWeight: "600",
    cursor: "pointer",
  };

  const categoryBarStyle = {
    display: "flex",
    justifyContent: "center",
    gap: "12px",
    padding: "40px 24px 30px 24px",
    backgroundColor: "var(--white)",
    borderBottom: "1px solid #f0f3f1",
    flexWrap: "wrap",
  };

  const categoryPillStyle = (isActive) => ({
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "center",
    width: "80px",
    height: "80px",
    borderRadius: "8px",
    border: `1.5px solid ${isActive ? "var(--primary-green)" : "#e4e7ec"}`,
    backgroundColor: isActive ? "var(--primary-green)" : "var(--white)",
    color: isActive ? "var(--white)" : "var(--text-muted)",
    cursor: "pointer",
    fontSize: "12px",
    fontWeight: "600",
    transition: "all 0.2s ease",
  });

  const categoryIconStyle = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    marginBottom: "6px",
    color: "inherit",
  };

  const sectionStyle = {
    padding: "60px 8%",
    backgroundColor: "var(--light-green-bg)",
  };

  const sectionHeaderStyle = {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "flex-end",
    marginBottom: "32px",
  };

  const pretitleStyle = {
    color: "#e67e22",
    fontSize: "12px",
    fontWeight: "700",
    textTransform: "uppercase",
    letterSpacing: "1.5px",
    marginBottom: "6px",
  };

  const titleStyle = {
    fontSize: "32px",
    fontWeight: "800",
    color: "#0f172a",
    lineHeight: "1.2",
  };

  const subtitle2Style = {
    fontSize: "15px",
    color: "var(--text-muted)",
    marginTop: "4px",
  };

  const viewAllStyle = {
    fontSize: "14px",
    fontWeight: "600",
    color: "var(--text-medium)",
    cursor: "pointer",
    border: "1px solid #cbd5e1",
    padding: "6px 12px",
    borderRadius: "4px",
    backgroundColor: "var(--white)",
  };

  const emptyContainerStyle = {
    padding: "40px 0",
    color: "var(--text-medium)",
    fontSize: "16px",
    fontWeight: "500",
  };

  const ctaSectionStyle = {
    backgroundColor: "var(--primary-green)",
    color: "var(--white)",
    textAlign: "center",
    padding: "80px 24px",
  };

  const ctaTitleStyle = {
    fontSize: "36px",
    fontWeight: "800",
    marginBottom: "12px",
  };

  const ctaSubtitleStyle = {
    fontSize: "16px",
    marginBottom: "32px",
    opacity: "0.9",
  };

  const ctaButtonsStyle = {
    display: "flex",
    justifyContent: "center",
    gap: "16px",
  };

  const ctaPrimaryBtnStyle = {
    display: "flex",
    alignItems: "center",
    backgroundColor: "var(--white)",
    color: "var(--primary-green)",
    padding: "12px 28px",
    borderRadius: "8px",
    fontWeight: "600",
    fontSize: "15px",
    boxShadow: "0 4px 6px rgba(0, 0, 0, 0.05)",
  };

  const ctaSecondaryBtnStyle = {
    border: "1px solid var(--white)",
    color: "var(--white)",
    padding: "12px 28px",
    borderRadius: "8px",
    fontWeight: "600",
    fontSize: "15px",
  };

  return (
    <div>
      {/* Hero Section */}
      <section style={heroStyle}>
        <div style={badgeStyle}>
          <DiamondIcon />
          Discover authentic local experiences
        </div>
        <h1 style={headingStyle}>
          Discover Local Places <br /> with <span style={{ color: "var(--accent-gold)", fontStyle: "italic" }}>LocalLens</span>
        </h1>
        <p style={subtitleStyle}>
          Uncover hidden gems, savor traditional flavors, and experience destinations through the eyes of those who call them home.
        </p>
        <div style={searchContainerStyle}>
          <input
            type="text"
            placeholder="Search destinations, cities, or experiences..."
            style={searchInputStyle}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
          <button style={exploreBtnStyle} onClick={() => navigate("/places")}>
            <ExploreIcon />
            Explore Places
          </button>
        </div>
      </section>

      {/* Category Selection Bar */}
      <section style={categoryBarStyle}>
        {categories.map((cat) => (
          <div
            key={cat.name}
            style={categoryPillStyle(selectedCategory === cat.name)}
            onClick={() => setSelectedCategory(cat.name)}
          >
            <span style={categoryIconStyle}>{cat.icon}</span>
            <span>{cat.name}</span>
          </div>
        ))}
      </section>

      {/* Curated Featured Destinations Section */}
      <section style={sectionStyle}>
        <div style={sectionHeaderStyle}>
          <div>
            <div style={pretitleStyle}>Curated For You</div>
            <h2 style={titleStyle}>Featured Destinations</h2>
            <div style={subtitle2Style}>Admin-approved places added by local guides</div>
          </div>
          <button style={viewAllStyle} onClick={() => navigate("/places")}>
            View all &gt;
          </button>
        </div>

        <div style={emptyContainerStyle}>
          No places found for {selectedCategory}.
        </div>
      </section>

      {/* Hidden Gems Section */}
      <section style={{ ...sectionStyle, borderTop: "1px solid #e4e7ec" }}>
        <div style={sectionHeaderStyle}>
          <div>
            <div style={pretitleStyle}>Hidden Gems</div>
            <h2 style={titleStyle}>Off the Beaten Path</h2>
          </div>
          <button style={viewAllStyle} onClick={() => navigate("/places")}>
            View all &gt;
          </button>
        </div>

        <div style={emptyContainerStyle}>
          No hidden gems found.
        </div>
      </section>

      {/* Share Your Local Knowledge CTA Section */}
      <section style={ctaSectionStyle}>
        <h2 style={ctaTitleStyle}>Share Your Local Knowledge</h2>
        <p style={ctaSubtitleStyle}>
          Are you a local guide? Help travellers discover authentic places in your city.
        </p>
        <div style={ctaButtonsStyle}>
          <button style={ctaPrimaryBtnStyle} onClick={() => navigate("/register")}>
            <MapPinIcon />
            Contribute a Place
          </button>
          <button style={ctaSecondaryBtnStyle} onClick={() => navigate("/places")}>
            Start Exploring
          </button>
        </div>
      </section>
    </div>
  );
}

export default Discover;
