import React, { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { Camera, Save, X, Globe, MapPin, User, CheckCircle } from "lucide-react";

function GuideProfile() {
  const { currentUser } = useAuth();
  const [profileImage, setProfileImage] = useState(null);
  const [sameAsPermanent, setSameAsPermanent] = useState(true);
  const [toast, setToast] = useState(null);
  const [saving, setSaving] = useState(false);

  const [form, setForm] = useState({
    firstName: currentUser?.firstName || "",
    lastName: currentUser?.lastName || "",
    email: currentUser?.email || "",
    phone: "",
    gender: "",
    dob: "",
    occupation: "",
    experienceYears: "",
    expertise: "",
    languages: "",
    bio: "",
    permLine1: "", permLine2: "", permLine3: "",
    permArea: "", permCity: "", permState: "", permZip: "",
    currLine1: "", currLine2: "", currLine3: "",
    currArea: "", currCity: "", currState: "", currZip: "",
    instagram: "", facebook: "", website: "",
  });

  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3500);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    if (file.size > 5 * 1024 * 1024) { showToast("Image must be under 5MB", "error"); return; }
    const reader = new FileReader();
    reader.onloadend = () => { setProfileImage(reader.result); showToast("Profile photo updated!"); };
    reader.readAsDataURL(file);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    // Simulate API call — integrate with backend here
    await new Promise(r => setTimeout(r, 800));
    setSaving(false);
    showToast("Profile changes saved successfully!");
  };

  const handleCancel = () => showToast("Changes discarded", "info");

  const inputStyle = {
    width: "100%", padding: "11px 14px", borderRadius: "8px",
    border: "1px solid var(--border-color)", outline: "none",
    fontSize: "14px", color: "var(--text-dark)", backgroundColor: "#ffffff",
    boxSizing: "border-box",
  };
  const readOnlyStyle = { ...inputStyle, backgroundColor: "#f3f4f6", color: "#6b7280", cursor: "not-allowed" };
  const labelStyle = { display: "block", marginBottom: "6px", fontSize: "13px", fontWeight: "700", color: "var(--text-medium)" };
  const sectionStyle = { fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", borderBottom: "2px solid #eaf6ef", paddingBottom: "8px", marginBottom: "20px", marginTop: "28px", display: "flex", alignItems: "center", gap: "8px" };
  const gridStyle = { display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))", gap: "18px", marginBottom: "8px" };

  return (
    <div style={{ maxWidth: "960px", margin: "0 auto", paddingBottom: "40px" }}>
      {/* Toast */}
      {toast && (
        <div style={{ position: "fixed", bottom: "24px", right: "24px", backgroundColor: toast.type === "error" ? "#ef4444" : toast.type === "info" ? "#3b82f6" : "var(--primary-green)", color: "white", padding: "12px 24px", borderRadius: "10px", boxShadow: "0 10px 30px rgba(0,0,0,0.2)", display: "flex", alignItems: "center", gap: "10px", zIndex: 2000, fontSize: "14px", fontWeight: "600" }}>
          <CheckCircle size={18} /> {toast.msg}
        </div>
      )}

      <div style={{ marginBottom: "24px" }}>
        <h1 style={{ fontSize: "24px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>My Profile</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Manage your guide profile, address, and social channels.</p>
      </div>

      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", overflow: "hidden" }}>
        <form onSubmit={handleSubmit}>
          {/* Hero Banner */}
          <div style={{ height: "130px", background: "linear-gradient(135deg, var(--primary-green), var(--dark-green))", position: "relative" }}>
            <div style={{ position: "absolute", bottom: "-45px", left: "32px" }}>
              <div style={{ position: "relative" }}>
                <div style={{ width: "100px", height: "100px", borderRadius: "50%", border: "4px solid white", backgroundColor: "var(--primary-green)", backgroundImage: profileImage ? `url(${profileImage})` : "none", backgroundSize: "cover", backgroundPosition: "center", display: "flex", alignItems: "center", justifyContent: "center", color: "white", fontSize: "36px", fontWeight: "800", boxShadow: "0 4px 12px rgba(0,0,0,0.15)" }}>
                  {!profileImage && (currentUser?.firstName?.charAt(0) || "G")}
                </div>
                <label htmlFor="photo-upload" style={{ position: "absolute", bottom: "4px", right: "4px", backgroundColor: "white", borderRadius: "50%", padding: "8px", boxShadow: "0 2px 6px rgba(0,0,0,0.2)", cursor: "pointer", display: "flex" }} title="Upload Profile Photo (Base64)">
                  <Camera size={16} color="var(--primary-green)" />
                </label>
                <input type="file" id="photo-upload" accept="image/*" style={{ display: "none" }} onChange={handleImageUpload} />
              </div>
            </div>
          </div>

          <div style={{ padding: "56px 32px 32px" }}>
            {/* Basic Information */}
            <div style={sectionStyle}><User size={18} color="var(--primary-green)" /> Basic Information</div>
            <div style={gridStyle}>
              <div><label style={labelStyle}>First Name</label><input type="text" name="firstName" value={form.firstName} onChange={handleChange} style={inputStyle} required /></div>
              <div><label style={labelStyle}>Last Name</label><input type="text" name="lastName" value={form.lastName} onChange={handleChange} style={inputStyle} required /></div>
              <div><label style={labelStyle}>Email Address (Read Only)</label><input type="email" value={form.email} readOnly style={readOnlyStyle} /></div>
              <div><label style={labelStyle}>Phone Number</label><input type="tel" name="phone" value={form.phone} onChange={handleChange} style={inputStyle} placeholder="+91 98765 43210" /></div>
              <div>
                <label style={labelStyle}>Gender</label>
                <select name="gender" value={form.gender} onChange={handleChange} style={inputStyle}>
                  <option value="">Select gender...</option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                  <option value="Prefer not to say">Prefer not to say</option>
                </select>
              </div>
              <div><label style={labelStyle}>Date of Birth</label><input type="date" name="dob" value={form.dob} onChange={handleChange} style={inputStyle} /></div>
              <div><label style={labelStyle}>Occupation</label><input type="text" name="occupation" value={form.occupation} onChange={handleChange} style={inputStyle} placeholder="e.g. Licensed Tour Guide" /></div>
              <div><label style={labelStyle}>Years of Experience</label><input type="number" name="experienceYears" value={form.experienceYears} onChange={handleChange} style={inputStyle} min="0" /></div>
              <div style={{ gridColumn: "1 / -1" }}><label style={labelStyle}>Areas of Expertise</label><input type="text" name="expertise" value={form.expertise} onChange={handleChange} style={inputStyle} placeholder="e.g. History, Food Tours, Trekking" /></div>
              <div style={{ gridColumn: "1 / -1" }}><label style={labelStyle}>Languages Spoken</label><input type="text" name="languages" value={form.languages} onChange={handleChange} style={inputStyle} placeholder="e.g. English, Spanish, French" /></div>
              <div style={{ gridColumn: "1 / -1" }}><label style={labelStyle}>About Me / Bio</label><textarea name="bio" value={form.bio} onChange={handleChange} rows={4} style={{ ...inputStyle, fontFamily: "inherit", resize: "vertical" }} placeholder="Introduce yourself to travelers..." /></div>
            </div>

            {/* Permanent Address */}
            <div style={sectionStyle}><MapPin size={18} color="var(--primary-green)" /> Permanent Address</div>
            <div style={gridStyle}>
              <div style={{ gridColumn: "1 / -1" }}><label style={labelStyle}>Address Line 1</label><input type="text" name="permLine1" value={form.permLine1} onChange={handleChange} style={inputStyle} /></div>
              <div><label style={labelStyle}>Address Line 2</label><input type="text" name="permLine2" value={form.permLine2} onChange={handleChange} style={inputStyle} /></div>
              <div><label style={labelStyle}>Address Line 3</label><input type="text" name="permLine3" value={form.permLine3} onChange={handleChange} style={inputStyle} /></div>
              <div><label style={labelStyle}>Area</label><input type="text" name="permArea" value={form.permArea} onChange={handleChange} style={inputStyle} /></div>
              <div><label style={labelStyle}>City</label><input type="text" name="permCity" value={form.permCity} onChange={handleChange} style={inputStyle} /></div>
              <div><label style={labelStyle}>State</label><input type="text" name="permState" value={form.permState} onChange={handleChange} style={inputStyle} /></div>
              <div><label style={labelStyle}>Zip Code</label><input type="text" name="permZip" value={form.permZip} onChange={handleChange} style={inputStyle} /></div>
            </div>

            {/* Current Address */}
            <div style={{ margin: "24px 0", padding: "16px", backgroundColor: "#f9fafb", borderRadius: "10px", border: "1px solid var(--border-color)" }}>
              <label style={{ display: "flex", alignItems: "center", gap: "10px", cursor: "pointer", fontSize: "14px", fontWeight: "600", color: "var(--text-dark)" }}>
                <input type="checkbox" checked={sameAsPermanent} onChange={e => setSameAsPermanent(e.target.checked)} style={{ width: "18px", height: "18px", accentColor: "var(--primary-green)" }} />
                Current Address is same as Permanent Address
              </label>
              {!sameAsPermanent && (
                <div style={{ ...gridStyle, marginTop: "20px" }}>
                  <div style={{ gridColumn: "1 / -1" }}><label style={labelStyle}>Address Line 1</label><input type="text" name="currLine1" value={form.currLine1} onChange={handleChange} style={inputStyle} /></div>
                  <div><label style={labelStyle}>Address Line 2</label><input type="text" name="currLine2" value={form.currLine2} onChange={handleChange} style={inputStyle} /></div>
                  <div><label style={labelStyle}>Area</label><input type="text" name="currArea" value={form.currArea} onChange={handleChange} style={inputStyle} /></div>
                  <div><label style={labelStyle}>City</label><input type="text" name="currCity" value={form.currCity} onChange={handleChange} style={inputStyle} /></div>
                  <div><label style={labelStyle}>State</label><input type="text" name="currState" value={form.currState} onChange={handleChange} style={inputStyle} /></div>
                  <div><label style={labelStyle}>Zip Code</label><input type="text" name="currZip" value={form.currZip} onChange={handleChange} style={inputStyle} /></div>
                </div>
              )}
            </div>

            {/* Social Links */}
            <div style={sectionStyle}><Globe size={18} color="var(--primary-green)" /> Social Links</div>
            <div style={gridStyle}>
              <div><label style={labelStyle}>Instagram</label><div style={{ position: "relative" }}><svg width="16" height="16" viewBox="0 0 24 24" fill="none" style={{ position: "absolute", left: "12px", top: "13px", color: "#e1306c" }}><rect x="2" y="2" width="20" height="20" rx="5" stroke="#e1306c" strokeWidth="2" fill="none"/><circle cx="12" cy="12" r="5" stroke="#e1306c" strokeWidth="2" fill="none"/><circle cx="17.5" cy="6.5" r="1.5" fill="#e1306c"/></svg><input type="url" name="instagram" value={form.instagram} onChange={handleChange} style={{ ...inputStyle, paddingLeft: "36px" }} placeholder="https://instagram.com/..." /></div></div>
              <div><label style={labelStyle}>Facebook</label><div style={{ position: "relative" }}><svg width="16" height="16" viewBox="0 0 24 24" fill="#1877f2" style={{ position: "absolute", left: "12px", top: "13px" }}><path d="M18 2h-3a5 5 0 00-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 011-1h3z"/></svg><input type="url" name="facebook" value={form.facebook} onChange={handleChange} style={{ ...inputStyle, paddingLeft: "36px" }} placeholder="https://facebook.com/..." /></div></div>
              <div><label style={labelStyle}>Website / Portfolio</label><div style={{ position: "relative" }}><Globe size={16} style={{ position: "absolute", left: "12px", top: "13px", color: "var(--primary-green)" }} /><input type="url" name="website" value={form.website} onChange={handleChange} style={{ ...inputStyle, paddingLeft: "36px" }} placeholder="https://yoursite.com" /></div></div>
            </div>

            {/* Action Buttons */}
            <div style={{ display: "flex", justifyContent: "flex-end", gap: "12px", borderTop: "1px solid var(--border-color)", paddingTop: "24px", marginTop: "24px" }}>
              <button type="button" onClick={handleCancel} style={{ display: "flex", alignItems: "center", gap: "8px", padding: "11px 22px", borderRadius: "8px", border: "1px solid var(--border-color)", backgroundColor: "var(--white)", color: "var(--text-medium)", fontWeight: "600", fontSize: "14px", cursor: "pointer" }}>
                <X size={18} /> Cancel
              </button>
              <button type="submit" disabled={saving} style={{ display: "flex", alignItems: "center", gap: "8px", padding: "11px 24px", backgroundColor: saving ? "#9dc5b2" : "var(--primary-green)", color: "white", borderRadius: "8px", fontWeight: "700", fontSize: "14px", cursor: saving ? "not-allowed" : "pointer", border: "none" }}>
                <Save size={18} /> {saving ? "Saving..." : "Save Changes"}
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default GuideProfile;
