import React from "react";
import { Link } from "react-router-dom";

function Footer() {
  // SVG Icons for Contact info
  const MailIcon = () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ marginRight: "10px", flexShrink: 0 }}>
      <path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 8L12 13L4 8V6L12 11L20 6V8Z" fill="currentColor" />
    </svg>
  );

  const PhoneIcon = () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ marginRight: "10px", flexShrink: 0 }}>
      <path d="M6.62 10.79C8.06 13.62 10.38 15.93 13.21 17.38L15.41 15.18C15.69 14.9 16.08 14.82 16.43 14.93C17.55 15.3 18.75 15.5 20 15.5C20.55 15.5 21 15.95 21 16.5V20C21 20.55 20.55 21 20 21C8.95 21 0 12.05 0 1C0 .45.45 0 1 0H4.5C5.05 0 5.5 .45 5.5 1C5.5 2.25 5.7 3.45 6.07 4.57C6.18 4.92 6.1 5.31 5.82 5.59L3.62 7.79C5.07 10.63 7.39 12.94 10.23 14.38L12.43 12.18C12.71 11.9 13.1 11.82 13.45 11.93C14.57 12.3 15.77 12.5 17 12.5C17.55 12.5 18 12.95 18 13.5V17C18 17.55 17.55 18 17 18C5.95 18 -3 9.05 -3 -2" fill="currentColor" />
    </svg>
  );

  const GlobeIcon = () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ marginRight: "10px", flexShrink: 0 }}>
      <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 4C13.2 5.5 14.1 7.2 14.7 9H9.3C9.9 7.2 10.8 5.5 12 4ZM4.26 14C4.1 13.4 4 12.7 4 12C4 11.3 4.1 10.6 4.26 10H8.13C8.05 10.7 8 11.3 8 12C8 12.7 8.05 13.3 8.13 14H4.26ZM12 20C10.8 18.5 9.9 16.8 9.3 15H14.7C14.1 16.8 13.2 18.5 12 20ZM9.85 14C9.94 13.4 10 12.7 10 12C10 11.3 9.94 10.6 9.85 10H14.15C14.06 10.7 14 11.3 14 12C14 12.7 14.06 13.3 14.15 14H9.85ZM19.74 10C19.9 10.6 20 11.3 20 12C20 12.7 19.9 13.4 19.74 14H15.87C15.95 13.3 16 12.7 16 12C16 11.3 15.95 10.7 15.87 10H19.74ZM15.89 4.81C17.4 6 18.6 7.4 19.3 9H15.89C15.39 7.4 14.6 6 13.63 4.81ZM10.37 4.81C9.4 6 8.61 7.4 8.11 9H4.7C5.4 7.4 6.6 6 8.11 4.81ZM4.7 15H8.11C8.61 16.6 9.4 18 10.37 19.19C8.61 18 7.4 16.6 4.7 15ZM13.63 19.19C14.6 18 15.39 16.6 15.89 15H19.3C18.6 16.6 17.4 18 15.89 19.19Z" fill="currentColor" />
    </svg>
  );

  const PinIcon = () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg" style={{ marginRight: "10px", flexShrink: 0 }}>
      <path d="M12 2C8.13 2 5 5.13 5 9C5 14.25 12 22 12 22C12 22 19 14.25 19 9C19 5.13 15.87 2 12 2ZM12 11.5C10.62 11.5 9.5 10.38 9.5 9C9.5 7.62 10.62 6.5 12 6.5C13.38 6.5 14.5 7.62 14.5 9C14.5 10.38 13.38 11.5 12 11.5Z" fill="currentColor" />
    </svg>
  );

  // Layout Styles
  const footerStyle = {
    backgroundColor: "var(--dark-green)",
    color: "rgba(255, 255, 255, 0.8)",
    padding: "80px 8% 40px 8%",
    fontSize: "14px",
    lineHeight: "1.6",
  };

  const gridStyle = {
    display: "grid",
    gridTemplateColumns: "2fr 1fr 1fr 1.5fr",
    gap: "48px",
    marginBottom: "60px",
  };

  const logoStyle = {
    display: "flex",
    alignItems: "center",
    fontSize: "22px",
    fontWeight: "800",
    color: "var(--white)",
    marginBottom: "20px",
  };

  const descriptionStyle = {
    marginBottom: "24px",
    maxWidth: "320px",
  };

  const socialContainerStyle = {
    display: "flex",
    gap: "8px",
  };

  const socialBtnStyle = {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    width: "36px",
    height: "36px",
    backgroundColor: "rgba(255, 255, 255, 0.1)",
    borderRadius: "4px",
    color: "var(--white)",
    fontWeight: "bold",
    fontSize: "14px",
    transition: "background-color 0.2s ease",
  };

  const columnTitleStyle = {
    color: "var(--white)",
    fontSize: "15px",
    fontWeight: "700",
    marginBottom: "24px",
    textTransform: "uppercase",
    letterSpacing: "0.8px",
  };

  const listStyle = {
    listStyle: "none",
    padding: 0,
    margin: 0,
  };

  const listItemStyle = {
    marginBottom: "14px",
  };

  const linkStyle = {
    cursor: "pointer",
    transition: "color 0.2s ease",
  };

  const contactItemStyle = {
    display: "flex",
    alignItems: "flex-start",
    marginBottom: "16px",
  };

  const bottomStyle = {
    borderTop: "1px solid rgba(255, 255, 255, 0.1)",
    paddingTop: "30px",
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    flexWrap: "wrap",
    gap: "16px",
  };

  const bottomLinksStyle = {
    display: "flex",
    gap: "24px",
  };

  return (
    <footer style={footerStyle}>
      <div style={gridStyle}>
        {/* Branding Column */}
        <div>
          <div style={logoStyle}>
            <div style={{ color: "var(--white)", display: "flex", alignItems: "center", marginRight: "8px" }}>
              <PinIcon />
            </div>
            <span>LocalLens</span>
          </div>
          <p style={descriptionStyle}>
            Discover authentic local experiences. Travel like a local — uncover hidden gems, savor traditional flavors, and explore destinations through the eyes of those who call them home.
          </p>
          <div style={socialContainerStyle}>
            <button style={socialBtnStyle}>F</button>
            <button style={socialBtnStyle}>T</button>
            <button style={socialBtnStyle}>I</button>
            <button style={socialBtnStyle}>Y</button>
          </div>
        </div>

        {/* Quick Links Column */}
        <div>
          <h4 style={columnTitleStyle}>Quick Links</h4>
          <ul style={listStyle}>
            <li style={listItemStyle}>
              <Link to="/" style={linkStyle}>Home</Link>
            </li>
            <li style={listItemStyle}>
              <Link to="/about" style={linkStyle}>About Us</Link>
            </li>
            <li style={listItemStyle}>
              <Link to="/places" style={linkStyle}>Places</Link>
            </li>
            <li style={listItemStyle}>
              <Link to="/contact" style={linkStyle}>Contact</Link>
            </li>
            <li style={listItemStyle}>
              <Link to="/register" style={linkStyle}>Register</Link>
            </li>
            <li style={listItemStyle}>
              <Link to="/login" style={linkStyle}>Login</Link>
            </li>
          </ul>
        </div>

        {/* Explore Column */}
        <div>
          <h4 style={columnTitleStyle}>Explore</h4>
          <ul style={listStyle}>
            <li style={listItemStyle}><span style={linkStyle}>Hidden Gems</span></li>
            <li style={listItemStyle}><span style={linkStyle}>Local Cuisines</span></li>
            <li style={listItemStyle}><span style={linkStyle}>Adventure Spots</span></li>
            <li style={listItemStyle}><span style={linkStyle}>Cultural Sites</span></li>
            <li style={listItemStyle}><span style={linkStyle}>Travel Tips</span></li>
            <li style={listItemStyle}><span style={linkStyle}>Local Guides</span></li>
          </ul>
        </div>

        {/* Contact Column */}
        <div>
          <h4 style={columnTitleStyle}>Contact</h4>
          <div style={contactItemStyle}>
            <MailIcon />
            <span>hello@locallens.com</span>
          </div>
          <div style={contactItemStyle}>
            <PhoneIcon />
            <span>+1 (555) 123-4567</span>
          </div>
          <div style={contactItemStyle}>
            <GlobeIcon />
            <span>www.locallens.com</span>
          </div>
          <div style={contactItemStyle}>
            <PinIcon />
            <span>123 Explorer Street, Travel City, TC 56789</span>
          </div>
        </div>
      </div>

      {/* Bottom Footer Bar */}
      <div style={bottomStyle}>
        <div>© 2026 LocalLens. All rights reserved.</div>
        <div style={bottomLinksStyle}>
          <a href="#" style={linkStyle} onClick={(e) => e.preventDefault()}>Privacy Policy</a>
          <a href="#" style={linkStyle} onClick={(e) => e.preventDefault()}>Terms of Service</a>
          <a href="#" style={linkStyle} onClick={(e) => e.preventDefault()}>Cookie Policy</a>
        </div>
      </div>
    </footer>
  );
}

export default Footer;