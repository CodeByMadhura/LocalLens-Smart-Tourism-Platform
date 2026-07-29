import React, { useState } from "react";
import { Bell, Shield, Save, CheckCircle } from "lucide-react";

function GuideSettings() {
  const [settings, setSettings] = useState({
    emailNotifs: true,
    reviewAlerts: true,
    approvalAlerts: true,
    marketingEmails: false,
  });
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [toast, setToast] = useState(false);
  const [saving, setSaving] = useState(false);

  const toggle = (key) => setSettings(prev => ({ ...prev, [key]: !prev[key] }));

  const handleSave = async (e) => {
    e.preventDefault();
    setSaving(true);
    await new Promise(r => setTimeout(r, 700));
    setSaving(false);
    setToast(true);
    setTimeout(() => setToast(false), 3000);
    setCurrentPassword("");
    setNewPassword("");
  };

  const Toggle = ({ checked, onChange }) => (
    <div onClick={onChange} style={{ width: "44px", height: "24px", borderRadius: "12px", backgroundColor: checked ? "var(--primary-green)" : "#d1d5db", position: "relative", cursor: "pointer", transition: "background 0.2s", flexShrink: 0 }}>
      <div style={{ width: "20px", height: "20px", borderRadius: "50%", backgroundColor: "white", position: "absolute", top: "2px", left: checked ? "22px" : "2px", transition: "left 0.2s", boxShadow: "0 1px 4px rgba(0,0,0,0.2)" }} />
    </div>
  );

  return (
    <div style={{ maxWidth: "780px", margin: "0 auto", paddingBottom: "40px" }}>
      {toast && (
        <div style={{ position: "fixed", bottom: "24px", right: "24px", backgroundColor: "var(--primary-green)", color: "white", padding: "12px 22px", borderRadius: "10px", boxShadow: "0 10px 30px rgba(0,0,0,0.2)", display: "flex", alignItems: "center", gap: "10px", zIndex: 2000, fontSize: "14px", fontWeight: "600" }}>
          <CheckCircle size={18} /> Settings saved!
        </div>
      )}

      <div style={{ marginBottom: "28px" }}>
        <h1 style={{ fontSize: "24px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Account Settings</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Manage notifications and security preferences.</p>
      </div>

      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "32px" }}>
        <form onSubmit={handleSave}>

          {/* Notifications */}
          <div style={{ display: "flex", alignItems: "center", gap: "8px", fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", borderBottom: "1px solid var(--border-color)", paddingBottom: "10px", marginBottom: "20px" }}>
            <Bell size={18} color="var(--primary-green)" /> Notification Preferences
          </div>

          <div style={{ display: "flex", flexDirection: "column", gap: "18px", marginBottom: "32px" }}>
            {[
              { key: "emailNotifs", label: "Email Notifications", desc: "Receive emails when your places are approved or reviewed" },
              { key: "reviewAlerts", label: "Instant Review Alerts", desc: "Get notified immediately when a traveler leaves a review" },
              { key: "approvalAlerts", label: "Approval Status Alerts", desc: "Email when admin approves or rejects your submissions" },
              { key: "marketingEmails", label: "Tips & Marketing Emails", desc: "Receive helpful guides and LocalLens updates" },
            ].map(item => (
              <div key={item.key} style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "16px" }}>
                <div>
                  <p style={{ fontWeight: "600", color: "var(--text-dark)", margin: "0 0 2px 0", fontSize: "14px" }}>{item.label}</p>
                  <p style={{ fontSize: "12px", color: "var(--text-muted)", margin: 0 }}>{item.desc}</p>
                </div>
                <Toggle checked={settings[item.key]} onChange={() => toggle(item.key)} />
              </div>
            ))}
          </div>

          {/* Security */}
          <div style={{ display: "flex", alignItems: "center", gap: "8px", fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", borderBottom: "1px solid var(--border-color)", paddingBottom: "10px", marginBottom: "20px" }}>
            <Shield size={18} color="var(--primary-green)" /> Password & Security
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px", marginBottom: "28px" }}>
            <div>
              <label style={{ display: "block", marginBottom: "6px", fontSize: "13px", fontWeight: "700", color: "var(--text-medium)" }}>Current Password</label>
              <input type="password" placeholder="••••••••" value={currentPassword} onChange={e => setCurrentPassword(e.target.value)} style={{ width: "100%", padding: "10px 14px", borderRadius: "8px", border: "1px solid var(--border-color)", outline: "none", fontSize: "14px", boxSizing: "border-box" }} />
            </div>
            <div>
              <label style={{ display: "block", marginBottom: "6px", fontSize: "13px", fontWeight: "700", color: "var(--text-medium)" }}>New Password</label>
              <input type="password" placeholder="••••••••" value={newPassword} onChange={e => setNewPassword(e.target.value)} style={{ width: "100%", padding: "10px 14px", borderRadius: "8px", border: "1px solid var(--border-color)", outline: "none", fontSize: "14px", boxSizing: "border-box" }} />
            </div>
          </div>

          <div style={{ display: "flex", justifyContent: "flex-end", borderTop: "1px solid var(--border-color)", paddingTop: "24px" }}>
            <button type="submit" disabled={saving} style={{ display: "flex", alignItems: "center", gap: "8px", padding: "12px 24px", backgroundColor: saving ? "#9dc5b2" : "var(--primary-green)", color: "white", borderRadius: "8px", fontWeight: "700", fontSize: "14px", cursor: saving ? "not-allowed" : "pointer", border: "none" }}>
              <Save size={18} /> {saving ? "Saving..." : "Save Settings"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default GuideSettings;
