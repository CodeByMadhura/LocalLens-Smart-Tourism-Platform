import React, { useState } from "react";
import {
  AreaChart, Area, BarChart, Bar, LineChart, Line, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from "recharts";
import { MOCK_ANALYTICS } from "./data/mockData";

const COLORS = ["#1d6d4a", "#dfb05b", "#1d4ed8", "#dc2626", "#7c3aed"];

function ChartCard({ title, subtitle, children }) {
  return (
    <div style={{ backgroundColor: "var(--white)", borderRadius: "14px", padding: "22px", boxShadow: "var(--card-shadow)", border: "1px solid #f0f3f1" }}>
      <div style={{ marginBottom: "20px" }}>
        <h3 style={{ fontSize: "16px", fontWeight: "700", color: "var(--text-dark)", margin: "0 0 2px 0" }}>{title}</h3>
        {subtitle && <p style={{ fontSize: "13px", color: "var(--text-muted)", margin: 0 }}>{subtitle}</p>}
      </div>
      {children}
    </div>
  );
}

const customTooltipStyle = {
  backgroundColor: "white",
  border: "1px solid #f0f3f1",
  borderRadius: "10px",
  padding: "10px 14px",
  boxShadow: "0 4px 20px rgba(0,0,0,0.1)",
  fontSize: "13px",
};

function AdminAnalytics() {
  const [activeTab, setActiveTab] = useState("overview");

  const tabs = [
    { id: "overview", label: "Overview" },
    { id: "growth", label: "User Growth" },
    { id: "places", label: "Places" },
    { id: "categories", label: "Categories" },
  ];

  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto" }}>
      <div style={{ marginBottom: "24px" }}>
        <h1 style={{ fontSize: "22px", fontWeight: "800", color: "var(--text-dark)", margin: "0 0 4px 0" }}>Analytics</h1>
        <p style={{ color: "var(--text-muted)", fontSize: "14px", margin: 0 }}>Platform performance insights and trends</p>
      </div>

      {/* Tabs */}
      <div style={{ display: "flex", gap: "4px", padding: "4px", backgroundColor: "#f0f4f2", borderRadius: "12px", marginBottom: "24px", width: "fit-content" }}>
        {tabs.map(tab => (
          <button
            key={tab.id}
            onClick={() => setActiveTab(tab.id)}
            style={{
              padding: "9px 18px",
              borderRadius: "9px",
              fontSize: "14px",
              fontWeight: "700",
              border: "none",
              cursor: "pointer",
              transition: "all 0.2s",
              backgroundColor: activeTab === tab.id ? "var(--white)" : "transparent",
              color: activeTab === tab.id ? "var(--primary-green)" : "var(--text-muted)",
              boxShadow: activeTab === tab.id ? "0 2px 8px rgba(0,0,0,0.07)" : "none",
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Overview Tab */}
      {activeTab === "overview" && (
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px" }}>
          <div style={{ gridColumn: "1 / -1" }}>
            <ChartCard title="Monthly Registrations" subtitle="New users joining LocalLens per month">
              <ResponsiveContainer width="100%" height={280}>
                <AreaChart data={MOCK_ANALYTICS.monthly}>
                  <defs>
                    <linearGradient id="regGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#1d6d4a" stopOpacity={0.18} />
                      <stop offset="95%" stopColor="#1d6d4a" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" />
                  <XAxis dataKey="month" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                  <Tooltip contentStyle={customTooltipStyle} />
                  <Area type="monotone" dataKey="registrations" stroke="#1d6d4a" strokeWidth={2.5} fill="url(#regGrad)" name="Registrations" dot={{ r: 4, fill: "#1d6d4a" }} />
                </AreaChart>
              </ResponsiveContainer>
            </ChartCard>
          </div>

          <ChartCard title="Most Active Guides" subtitle="Guides with most places uploaded">
            <ResponsiveContainer width="100%" height={240}>
              <BarChart data={MOCK_ANALYTICS.mostActiveGuides} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" horizontal={false} />
                <XAxis type="number" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <YAxis dataKey="name" type="category" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} width={90} />
                <Tooltip contentStyle={customTooltipStyle} />
                <Bar dataKey="places" fill="#1d6d4a" radius={[0, 6, 6, 0]} name="Places" />
              </BarChart>
            </ResponsiveContainer>
          </ChartCard>

          <ChartCard title="Top Categories" subtitle="Distribution of approved places by category">
            <div style={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
              <ResponsiveContainer width="100%" height={240}>
                <PieChart>
                  <Pie data={MOCK_ANALYTICS.topCategories} cx="50%" cy="50%" innerRadius={60} outerRadius={100} dataKey="value" nameKey="name">
                    {MOCK_ANALYTICS.topCategories.map((_, i) => (
                      <Cell key={i} fill={COLORS[i % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip contentStyle={customTooltipStyle} formatter={(val) => [`${val}%`, ""]} />
                  <Legend formatter={(value) => <span style={{ fontSize: "12px", color: "var(--text-medium)", fontWeight: "600" }}>{value}</span>} />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </ChartCard>
        </div>
      )}

      {/* Growth Tab */}
      {activeTab === "growth" && (
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px" }}>
          <div style={{ gridColumn: "1 / -1" }}>
            <ChartCard title="Guide vs Traveller Growth" subtitle="Comparative growth of both user types over time">
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={MOCK_ANALYTICS.travellerGrowth.map((t, i) => ({ ...t, guides: MOCK_ANALYTICS.guideGrowth[i].guides }))}>
                  <defs>
                    <linearGradient id="tGrad" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#1d4ed8" stopOpacity={0.15} />
                      <stop offset="95%" stopColor="#1d4ed8" stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" />
                  <XAxis dataKey="month" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                  <Tooltip contentStyle={customTooltipStyle} />
                  <Legend formatter={(value) => <span style={{ fontSize: "12px", fontWeight: "600" }}>{value}</span>} />
                  <Line type="monotone" dataKey="travellers" stroke="#1d4ed8" strokeWidth={2.5} dot={{ r: 5, fill: "#1d4ed8" }} name="Travellers" />
                  <Line type="monotone" dataKey="guides" stroke="#1d6d4a" strokeWidth={2.5} dot={{ r: 5, fill: "#1d6d4a" }} name="Guides" />
                </LineChart>
              </ResponsiveContainer>
            </ChartCard>
          </div>

          <ChartCard title="Guide Growth" subtitle="Monthly growth in registered guides">
            <ResponsiveContainer width="100%" height={240}>
              <AreaChart data={MOCK_ANALYTICS.guideGrowth}>
                <defs>
                  <linearGradient id="gGrad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#1d6d4a" stopOpacity={0.2} />
                    <stop offset="95%" stopColor="#1d6d4a" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <Tooltip contentStyle={customTooltipStyle} />
                <Area type="monotone" dataKey="guides" stroke="#1d6d4a" strokeWidth={2.5} fill="url(#gGrad)" name="Guides" dot={{ r: 4, fill: "#1d6d4a" }} />
              </AreaChart>
            </ResponsiveContainer>
          </ChartCard>

          <ChartCard title="Traveller Growth" subtitle="Monthly growth in registered travellers">
            <ResponsiveContainer width="100%" height={240}>
              <AreaChart data={MOCK_ANALYTICS.travellerGrowth}>
                <defs>
                  <linearGradient id="tvGrad" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#1d4ed8" stopOpacity={0.2} />
                    <stop offset="95%" stopColor="#1d4ed8" stopOpacity={0} />
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <Tooltip contentStyle={customTooltipStyle} />
                <Area type="monotone" dataKey="travellers" stroke="#1d4ed8" strokeWidth={2.5} fill="url(#tvGrad)" name="Travellers" dot={{ r: 4, fill: "#1d4ed8" }} />
              </AreaChart>
            </ResponsiveContainer>
          </ChartCard>
        </div>
      )}

      {/* Places Tab */}
      {activeTab === "places" && (
        <div style={{ display: "grid", gridTemplateColumns: "1fr", gap: "20px" }}>
          <ChartCard title="Monthly Place Activity" subtitle="Places uploaded, approved, and rejected per month">
            <ResponsiveContainer width="100%" height={320}>
              <BarChart data={MOCK_ANALYTICS.monthly}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" />
                <XAxis dataKey="month" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <YAxis tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <Tooltip contentStyle={customTooltipStyle} />
                <Legend formatter={(value) => <span style={{ fontSize: "12px", fontWeight: "600" }}>{value}</span>} />
                <Bar dataKey="placesUploaded" name="Uploaded" fill="#1d6d4a" radius={[4, 4, 0, 0]} />
                <Bar dataKey="approved" name="Approved" fill="#22c55e" radius={[4, 4, 0, 0]} />
                <Bar dataKey="rejected" name="Rejected" fill="#dc2626" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </ChartCard>

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px" }}>
            <ChartCard title="Approval Rate" subtitle="Ratio of approved to rejected places">
              <ResponsiveContainer width="100%" height={240}>
                <PieChart>
                  <Pie
                    data={[
                      { name: "Approved", value: 312 },
                      { name: "Rejected", value: 29 },
                      { name: "Pending", value: 47 },
                    ]}
                    cx="50%" cy="50%" innerRadius={55} outerRadius={95} dataKey="value"
                  >
                    {["#22c55e", "#dc2626", "#d97706"].map((color, i) => (
                      <Cell key={i} fill={color} />
                    ))}
                  </Pie>
                  <Tooltip contentStyle={customTooltipStyle} />
                  <Legend formatter={(value) => <span style={{ fontSize: "12px", fontWeight: "600" }}>{value}</span>} />
                </PieChart>
              </ResponsiveContainer>
            </ChartCard>

            <ChartCard title="Monthly Approval Trend" subtitle="Approved vs rejected over time">
              <ResponsiveContainer width="100%" height={240}>
                <LineChart data={MOCK_ANALYTICS.monthly}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" />
                  <XAxis dataKey="month" tick={{ fontSize: 11, fill: "#667085" }} axisLine={false} tickLine={false} />
                  <YAxis tick={{ fontSize: 11, fill: "#667085" }} axisLine={false} tickLine={false} />
                  <Tooltip contentStyle={customTooltipStyle} />
                  <Legend formatter={(value) => <span style={{ fontSize: "12px", fontWeight: "600" }}>{value}</span>} />
                  <Line type="monotone" dataKey="approved" stroke="#22c55e" strokeWidth={2.5} dot={{ r: 4 }} name="Approved" />
                  <Line type="monotone" dataKey="rejected" stroke="#dc2626" strokeWidth={2.5} dot={{ r: 4 }} name="Rejected" />
                </LineChart>
              </ResponsiveContainer>
            </ChartCard>
          </div>
        </div>
      )}

      {/* Categories Tab */}
      {activeTab === "categories" && (
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "20px" }}>
          <ChartCard title="Category Distribution" subtitle="Places by category (pie chart)">
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie data={MOCK_ANALYTICS.topCategories} cx="50%" cy="50%" outerRadius={110} dataKey="value" nameKey="name" label={({ name, value }) => `${name}: ${value}%`} labelLine={true}>
                  {MOCK_ANALYTICS.topCategories.map((_, i) => (
                    <Cell key={i} fill={COLORS[i % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip contentStyle={customTooltipStyle} formatter={(val) => [`${val}%`, ""]} />
              </PieChart>
            </ResponsiveContainer>
          </ChartCard>

          <ChartCard title="Top Categories by Volume" subtitle="Number of places per category">
            <ResponsiveContainer width="100%" height={300}>
              <BarChart data={MOCK_ANALYTICS.topCategories} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" stroke="#f0f3f1" horizontal={false} />
                <XAxis type="number" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} />
                <YAxis dataKey="name" type="category" tick={{ fontSize: 12, fill: "#667085" }} axisLine={false} tickLine={false} width={80} />
                <Tooltip contentStyle={customTooltipStyle} formatter={(val) => [`${val}%`, "Share"]} />
                <Bar dataKey="value" radius={[0, 6, 6, 0]} name="Share">
                  {MOCK_ANALYTICS.topCategories.map((_, i) => (
                    <Cell key={i} fill={COLORS[i % COLORS.length]} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </ChartCard>
        </div>
      )}
    </div>
  );
}

export default AdminAnalytics;
