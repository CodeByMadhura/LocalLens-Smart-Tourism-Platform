import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { MapPin, Image as ImageIcon, FileText, CheckCircle, ArrowRight, ArrowLeft, X } from "lucide-react";

function GuideAddPlace() {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [images, setImages] = useState([]);
  const [imagesError, setImagesError] = useState(false);
  const { register, handleSubmit, watch, trigger, formState: { errors } } = useForm({ mode: "onBlur" });
  const values = watch();

  const steps = [
    { num: 1, label: "Basic Details" },
    { num: 2, label: "Location" },
    { num: 3, label: "Images" },
    { num: 4, label: "Description" },
    { num: 5, label: "Preview" },
  ];

  const nextStep = async () => {
    let valid = false;
    if (step === 1) valid = await trigger(["name", "category", "price", "duration"]);
    else if (step === 2) valid = await trigger(["addressLine", "city", "state", "country"]);
    else if (step === 3) {
      if (images.length === 0) { setImagesError(true); return; }
      valid = true;
    } else if (step === 4) valid = await trigger(["description"]);
    if (valid) setStep(s => Math.min(s + 1, 5));
  };

  const onSubmit = (data) => {
    console.log("Submitting place:", { ...data, images });
    alert("Place submitted for admin approval!");
    navigate("/guide-dashboard/places");
  };

  const handleImages = (e) => {
    const files = Array.from(e.target.files);
    setImagesError(false);
    files.forEach(file => {
      if (file.size > 5 * 1024 * 1024) { alert(`${file.name} is too large.`); return; }
      const reader = new FileReader();
      reader.onloadend = () => setImages(prev => [...prev, reader.result]);
      reader.readAsDataURL(file);
    });
  };

  const removeImage = (i) => setImages(prev => prev.filter((_, idx) => idx !== i));

  const inputStyle = { width: "100%", padding: "11px 14px", borderRadius: "8px", border: "1px solid var(--border-color)", outline: "none", fontSize: "14px", color: "var(--text-dark)", backgroundColor: "#ffffff", boxSizing: "border-box" };
  const labelStyle = { display: "block", marginBottom: "6px", fontSize: "13px", fontWeight: "700", color: "var(--text-medium)" };
  const errStyle = { color: "#ef4444", fontSize: "12px", marginTop: "4px" };
  const grid2 = { display: "grid", gridTemplateColumns: "1fr 1fr", gap: "18px" };
  const btnStyle = (primary) => ({ display: "flex", alignItems: "center", gap: "8px", padding: "12px 22px", borderRadius: "8px", fontWeight: "700", fontSize: "14px", cursor: "pointer", border: "none", backgroundColor: primary ? "var(--primary-green)" : "#f3f4f6", color: primary ? "white" : "var(--text-dark)", transition: "all 0.2s" });

  return (
    <div style={{ maxWidth: "860px", margin: "0 auto", paddingBottom: "40px" }}>
      <div style={{ marginBottom: "28px" }}>
        <h1 style={{ fontSize: "24px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Add New Place</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Complete the 5-step form to submit a new destination for admin review.</p>
      </div>

      {/* Progress Stepper */}
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "36px", position: "relative" }}>
        <div style={{ position: "absolute", top: "18px", left: "5%", right: "5%", height: "2px", backgroundColor: "var(--border-color)", zIndex: 0 }} />
        <div style={{ position: "absolute", top: "18px", left: "5%", height: "2px", backgroundColor: "var(--primary-green)", zIndex: 1, width: `${((step - 1) / 4) * 90}%`, transition: "width 0.4s ease" }} />
        {steps.map(s => (
          <div key={s.num} style={{ display: "flex", flexDirection: "column", alignItems: "center", zIndex: 2, gap: "8px" }}>
            <div style={{ width: "36px", height: "36px", borderRadius: "50%", backgroundColor: step >= s.num ? "var(--primary-green)" : "var(--white)", color: step >= s.num ? "white" : "var(--text-muted)", border: step === s.num ? "3px solid var(--primary-green)" : `2px solid ${step > s.num ? "var(--primary-green)" : "var(--border-color)"}`, display: "flex", alignItems: "center", justifyContent: "center", fontWeight: "800", fontSize: "14px", boxShadow: step === s.num ? "0 0 0 4px rgba(29,109,74,0.2)" : "none", transition: "all 0.3s" }}>
              {step > s.num ? <CheckCircle size={18} /> : s.num}
            </div>
            <span style={{ fontSize: "11px", fontWeight: step >= s.num ? "700" : "500", color: step >= s.num ? "var(--text-dark)" : "var(--text-muted)" }}>{s.label}</span>
          </div>
        ))}
      </div>

      <div style={{ backgroundColor: "var(--white)", borderRadius: "12px", boxShadow: "var(--card-shadow)", padding: "36px" }}>
        <form onSubmit={handleSubmit(onSubmit)}>

          {/* Step 1 */}
          {step === 1 && (
            <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
              <h2 style={{ fontSize: "18px", fontWeight: "700", borderBottom: "1px solid var(--border-color)", paddingBottom: "10px", margin: "0 0 8px 0" }}>Basic Details</h2>
              <div><label style={labelStyle}>Place / Tour Title *</label><input type="text" style={inputStyle} placeholder="e.g. Hidden Bamboo Forest Walk" {...register("name", { required: "Title is required", minLength: { value: 5, message: "At least 5 characters" } })} />{errors.name && <p style={errStyle}>{errors.name.message}</p>}</div>
              <div>
                <label style={labelStyle}>Category *</label>
                <select style={inputStyle} {...register("category", { required: "Select a category" })}>
                  <option value="">Choose category...</option>
                  <option value="Cultural">Cultural & Heritage</option>
                  <option value="Food">Food & Culinary</option>
                  <option value="Nature">Nature & Wildlife</option>
                  <option value="Historical">Historical</option>
                  <option value="HiddenGems">Hidden Gems</option>
                  <option value="Adventure">Adventure</option>
                </select>
                {errors.category && <p style={errStyle}>{errors.category.message}</p>}
              </div>
              <div style={grid2}>
                <div><label style={labelStyle}>Price per Person (USD) *</label><input type="number" style={inputStyle} placeholder="e.g. 45" {...register("price", { required: "Price is required", min: { value: 0, message: "Must be ≥ 0" } })} />{errors.price && <p style={errStyle}>{errors.price.message}</p>}</div>
                <div><label style={labelStyle}>Duration (Hours) *</label><input type="number" step="0.5" style={inputStyle} placeholder="e.g. 3.5" {...register("duration", { required: "Duration is required", min: { value: 0.5, message: "Minimum 0.5h" } })} />{errors.duration && <p style={errStyle}>{errors.duration.message}</p>}</div>
              </div>
            </div>
          )}

          {/* Step 2 */}
          {step === 2 && (
            <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
              <h2 style={{ fontSize: "18px", fontWeight: "700", borderBottom: "1px solid var(--border-color)", paddingBottom: "10px", margin: "0 0 8px 0" }}>Location Details</h2>
              <div><label style={labelStyle}>Street Address / Meeting Point *</label><input type="text" style={inputStyle} placeholder="e.g. Near North Gate, Arashiyama Road" {...register("addressLine", { required: "Address is required" })} />{errors.addressLine && <p style={errStyle}>{errors.addressLine.message}</p>}</div>
              <div style={grid2}>
                <div><label style={labelStyle}>City *</label><input type="text" style={inputStyle} placeholder="e.g. Kyoto" {...register("city", { required: "City is required" })} />{errors.city && <p style={errStyle}>{errors.city.message}</p>}</div>
                <div><label style={labelStyle}>State / Province *</label><input type="text" style={inputStyle} placeholder="e.g. Kansai" {...register("state", { required: "State is required" })} />{errors.state && <p style={errStyle}>{errors.state.message}</p>}</div>
              </div>
              <div style={grid2}>
                <div><label style={labelStyle}>Country *</label><input type="text" style={inputStyle} placeholder="e.g. Japan" {...register("country", { required: "Country is required" })} />{errors.country && <p style={errStyle}>{errors.country.message}</p>}</div>
                <div><label style={labelStyle}>Zip / Postal Code</label><input type="text" style={inputStyle} placeholder="e.g. 600-8811" {...register("zipCode")} /></div>
              </div>
            </div>
          )}

          {/* Step 3 */}
          {step === 3 && (
            <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
              <h2 style={{ fontSize: "18px", fontWeight: "700", borderBottom: "1px solid var(--border-color)", paddingBottom: "10px", margin: "0 0 8px 0" }}>Upload Photos</h2>
              <input type="file" id="place-imgs" multiple accept="image/*" style={{ display: "none" }} onChange={handleImages} />
              <label htmlFor="place-imgs" style={{ border: "2px dashed var(--primary-green)", borderRadius: "12px", padding: "36px 20px", textAlign: "center", backgroundColor: "#eaf6ef", cursor: "pointer", display: "flex", flexDirection: "column", alignItems: "center", gap: "10px" }}>
                <div style={{ padding: "14px", backgroundColor: "white", borderRadius: "50%", boxShadow: "0 2px 6px rgba(0,0,0,0.1)" }}><ImageIcon size={28} color="var(--primary-green)" /></div>
                <span style={{ fontWeight: "700", color: "var(--text-dark)", fontSize: "15px" }}>Click or drag to upload images</span>
                <span style={{ fontSize: "12px", color: "var(--text-muted)" }}>PNG, JPG, WEBP • Max 5MB per file • Base64 encoded</span>
              </label>
              {imagesError && <p style={errStyle}>Please upload at least 1 image.</p>}
              {images.length > 0 && (
                <div>
                  <p style={{ fontSize: "13px", fontWeight: "700", marginBottom: "10px", color: "var(--text-dark)" }}>{images.length} image(s) uploaded</p>
                  <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(110px, 1fr))", gap: "10px" }}>
                    {images.map((img, i) => (
                      <div key={i} style={{ position: "relative", height: "90px", borderRadius: "8px", overflow: "hidden", border: "1px solid var(--border-color)" }}>
                        <img src={img} alt="" style={{ width: "100%", height: "100%", objectFit: "cover" }} />
                        <button type="button" onClick={() => removeImage(i)} style={{ position: "absolute", top: "4px", right: "4px", backgroundColor: "rgba(0,0,0,0.7)", color: "white", border: "none", borderRadius: "50%", padding: "4px", cursor: "pointer", display: "flex" }}><X size={14} /></button>
                      </div>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )}

          {/* Step 4 */}
          {step === 4 && (
            <div style={{ display: "flex", flexDirection: "column", gap: "20px" }}>
              <h2 style={{ fontSize: "18px", fontWeight: "700", borderBottom: "1px solid var(--border-color)", paddingBottom: "10px", margin: "0 0 8px 0" }}>Description</h2>
              <div><label style={labelStyle}>Full Description *</label><textarea rows={5} style={{ ...inputStyle, fontFamily: "inherit", resize: "vertical" }} placeholder="Describe the experience, what to expect, special instructions..." {...register("description", { required: "Description is required", minLength: { value: 30, message: "At least 30 characters required" } })} />{errors.description && <p style={errStyle}>{errors.description.message}</p>}</div>
              <div><label style={labelStyle}>Key Highlights (comma separated)</label><textarea rows={3} style={{ ...inputStyle, fontFamily: "inherit", resize: "vertical" }} placeholder="e.g. Scenic views, Local food, Private transport" {...register("highlights")} /></div>
            </div>
          )}

          {/* Step 5: Preview */}
          {step === 5 && (
            <div>
              <h2 style={{ fontSize: "18px", fontWeight: "700", borderBottom: "1px solid var(--border-color)", paddingBottom: "10px", marginBottom: "20px" }}>Preview & Submit</h2>
              <div style={{ backgroundColor: "#f9fafb", border: "1px solid var(--border-color)", borderRadius: "12px", overflow: "hidden" }}>
                {images.length > 0 ? <img src={images[0]} alt="Preview" style={{ width: "100%", height: "220px", objectFit: "cover" }} /> : <div style={{ width: "100%", height: "160px", backgroundColor: "#e5e7eb", display: "flex", alignItems: "center", justifyContent: "center", color: "#9ca3af" }}>No Image</div>}
                <div style={{ padding: "24px" }}>
                  <span style={{ display: "inline-block", padding: "4px 10px", backgroundColor: "#eaf6ef", color: "var(--primary-green)", borderRadius: "4px", fontSize: "12px", fontWeight: "700", marginBottom: "8px" }}>{values.category || "—"}</span>
                  <h3 style={{ fontSize: "20px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 8px 0" }}>{values.name || "Untitled"}</h3>
                  <p style={{ fontSize: "13px", color: "var(--text-muted)", display: "flex", alignItems: "center", gap: "4px", marginBottom: "12px" }}><MapPin size={14} /> {values.addressLine}, {values.city}, {values.country}</p>
                  <p style={{ fontSize: "14px", color: "var(--text-dark)", lineHeight: "1.6", marginBottom: "16px" }}>{values.description}</p>
                  <div style={{ display: "flex", gap: "20px", fontSize: "13px", color: "var(--text-muted)" }}>
                    <span>Price: <strong>${values.price}/person</strong></span>
                    <span>Duration: <strong>{values.duration}h</strong></span>
                    <span>Photos: <strong>{images.length}</strong></span>
                  </div>
                </div>
              </div>
              <div style={{ backgroundColor: "#eafaf1", borderRadius: "8px", padding: "14px 18px", marginTop: "16px", display: "flex", gap: "10px", alignItems: "center" }}>
                <CheckCircle size={20} color="#16a34a" />
                <span style={{ fontSize: "13px", color: "#16a34a", fontWeight: "600" }}>Ready to submit. Your place will go into admin review queue.</span>
              </div>
            </div>
          )}

          {/* Navigation */}
          <div style={{ display: "flex", justifyContent: "space-between", borderTop: "1px solid var(--border-color)", paddingTop: "24px", marginTop: "32px" }}>
            <button type="button" onClick={() => setStep(s => s - 1)} style={{ ...btnStyle(false), visibility: step === 1 ? "hidden" : "visible" }}>
              <ArrowLeft size={18} /> Previous
            </button>
            {step < 5 ? (
              <button type="button" onClick={nextStep} style={btnStyle(true)}>Next Step <ArrowRight size={18} /></button>
            ) : (
              <button type="submit" style={btnStyle(true)}><CheckCircle size={18} /> Submit Place</button>
            )}
          </div>
        </form>
      </div>
    </div>
  );
}

export default GuideAddPlace;
