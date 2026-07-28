// ============================================================
// Mock Data for Admin Dashboard (used until backend APIs ready)
// ============================================================

export const MOCK_STATS = {
  totalUsers: 1248,
  totalTravellers: 983,
  totalGuides: 265,
  pendingPlaces: 47,
  approvedPlaces: 312,
  rejectedPlaces: 29,
  reportedGuides: 8,
  activeUsers: 134,
};

export const MOCK_RECENT_ACTIVITIES = [
  { id: 1, type: "PLACE_REQUEST", message: "New place 'Mystic Cave Trail' submitted by Guide Arjun Mehta", time: "2 mins ago", icon: "MapPin" },
  { id: 2, type: "USER_REGISTER", message: "New traveller Priya Sharma registered", time: "15 mins ago", icon: "User" },
  { id: 3, type: "REPORT", message: "Guide 'Ravi Kumar' reported by 3 users", time: "1 hr ago", icon: "Flag" },
  { id: 4, type: "PLACE_APPROVED", message: "Place 'Sunset Cliff' approved by admin", time: "2 hrs ago", icon: "CheckCircle" },
  { id: 5, type: "PLACE_REJECTED", message: "Place 'Fake Paradise' rejected due to invalid content", time: "3 hrs ago", icon: "XCircle" },
  { id: 6, type: "USER_REGISTER", message: "New guide Kenji Sato registered & verified", time: "4 hrs ago", icon: "UserPlus" },
];

export const MOCK_LATEST_PLACES = [
  { id: 1, name: "Mystic Cave Trail", category: "Adventure", guide: "Arjun Mehta", city: "Shimla", submitted: "2026-07-28", status: "PENDING", img: "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=80&q=80" },
  { id: 2, name: "Ancient Pottery Workshop", category: "Cultural", guide: "Kenji Sato", city: "Jaipur", submitted: "2026-07-27", status: "PENDING", img: "https://images.unsplash.com/photo-1542044896530-05d85be9b11a?auto=format&fit=crop&w=80&q=80" },
  { id: 3, name: "Coastal Sunrise Trek", category: "Nature", guide: "Sam Wilson", city: "Goa", submitted: "2026-07-26", status: "APPROVED", img: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=80&q=80" },
  { id: 4, name: "Street Food Circuit", category: "Food", guide: "Giulia Rossi", city: "Mumbai", submitted: "2026-07-25", status: "REJECTED", img: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=80&q=80" },
];

export const MOCK_LATEST_USERS = [
  { id: 1, name: "Priya Sharma", email: "priya@example.com", role: "TRAVELLER", joined: "2026-07-28", avatar: "P" },
  { id: 2, name: "Marco Bianchi", email: "marco@example.com", role: "LOCAL_GUIDE", joined: "2026-07-27", avatar: "M" },
  { id: 3, name: "Aisha Khan", email: "aisha@example.com", role: "TRAVELLER", joined: "2026-07-27", avatar: "A" },
  { id: 4, name: "Takeshi Yamamoto", email: "takeshi@example.com", role: "LOCAL_GUIDE", joined: "2026-07-26", avatar: "T" },
];

export const MOCK_PLACES = [
  { id: 1, name: "Mystic Cave Trail", category: "Adventure", guide: "Arjun Mehta", city: "Shimla", submitted: "2026-07-28", status: "PENDING", img: "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=120&q=80", description: "A thrilling trek through ancient cave systems with stunning rock formations." },
  { id: 2, name: "Ancient Pottery Workshop", category: "Cultural", guide: "Kenji Sato", city: "Jaipur", submitted: "2026-07-27", status: "PENDING", img: "https://images.unsplash.com/photo-1542044896530-05d85be9b11a?auto=format&fit=crop&w=120&q=80", description: "Learn the traditional art of pottery from a 4th-generation craftsman." },
  { id: 3, name: "Coastal Sunrise Trek", category: "Nature", guide: "Sam Wilson", city: "Goa", submitted: "2026-07-26", status: "APPROVED", img: "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=120&q=80", description: "Experience the magical sunrise over the Arabian Sea from coastal cliffs." },
  { id: 4, name: "Street Food Circuit", category: "Food", guide: "Giulia Rossi", city: "Mumbai", submitted: "2026-07-25", status: "REJECTED", img: "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?auto=format&fit=crop&w=120&q=80", description: "A curated street food tour through the best local vendors." },
  { id: 5, name: "Hidden Forest Shrine", category: "Cultural", guide: "Ananya Iyer", city: "Ooty", submitted: "2026-07-24", status: "APPROVED", img: "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&w=120&q=80", description: "A serene forest shrine with centuries of history and local folklore." },
  { id: 6, name: "Blue Lagoon Rock Pools", category: "Nature", guide: "Kai Olsen", city: "Andaman", submitted: "2026-07-23", status: "PENDING", img: "https://images.unsplash.com/photo-1504280390367-361c6d9f38f4?auto=format&fit=crop&w=120&q=80", description: "Crystal clear natural rock pools with vibrant marine life." },
  { id: 7, name: "Grand Temple Ruins", category: "Historical", guide: "Ravi Kumar", city: "Hampi", submitted: "2026-07-22", status: "APPROVED", img: "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&w=120&q=80", description: "Majestic ruins of the Vijayanagara Empire, a UNESCO heritage site." },
  { id: 8, name: "Mountain Yak Safari", category: "Adventure", guide: "Tenzin Norbu", city: "Leh", submitted: "2026-07-21", status: "PENDING", img: "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?auto=format&fit=crop&w=120&q=80", description: "Ride yaks through breathtaking high-altitude Himalayan meadows." },
];

export const MOCK_TRAVELLERS = [
  { id: 1, name: "Priya Sharma", email: "priya@example.com", phone: "+91 98765 43210", city: "Bangalore", joined: "2026-07-28", status: "ACTIVE", avatar: "P" },
  { id: 2, name: "Marco Bianchi", email: "marco.b@example.com", phone: "+39 333 1234567", city: "Mumbai", joined: "2026-07-20", status: "ACTIVE", avatar: "M" },
  { id: 3, name: "Aisha Khan", email: "aisha.k@example.com", phone: "+92 321 9876543", city: "Delhi", joined: "2026-07-15", status: "DISABLED", avatar: "A" },
  { id: 4, name: "Takeshi Yamamoto", email: "takeshi@example.com", phone: "+81 90 1234 5678", city: "Chennai", joined: "2026-07-10", status: "ACTIVE", avatar: "T" },
  { id: 5, name: "Sofia Andersen", email: "sofia@example.com", phone: "+45 12 34 56 78", city: "Pune", joined: "2026-07-05", status: "ACTIVE", avatar: "S" },
  { id: 6, name: "Carlos Rivera", email: "carlos@example.com", phone: "+52 55 1234 5678", city: "Kolkata", joined: "2026-06-30", status: "DISABLED", avatar: "C" },
  { id: 7, name: "Amara Okafor", email: "amara@example.com", phone: "+234 802 345 6789", city: "Hyderabad", joined: "2026-06-25", status: "ACTIVE", avatar: "A" },
  { id: 8, name: "Linh Nguyen", email: "linh@example.com", phone: "+84 90 123 4567", city: "Jaipur", joined: "2026-06-20", status: "ACTIVE", avatar: "L" },
];

export const MOCK_GUIDES = [
  { id: 1, name: "Arjun Mehta", email: "arjun@example.com", phone: "+91 99887 76655", experience: "7 years", expertise: "Trekking, Adventure", verified: true, rating: 4.9, status: "ACTIVE", avatar: "A", places: 12 },
  { id: 2, name: "Kenji Sato", email: "kenji@example.com", phone: "+81 80 9876 5432", experience: "5 years", expertise: "Cultural, Historical", verified: true, rating: 4.8, status: "ACTIVE", avatar: "K", places: 8 },
  { id: 3, name: "Sam Wilson", email: "sam@example.com", phone: "+1 555 234 5678", experience: "3 years", expertise: "Nature, Wildlife", verified: true, rating: 4.7, status: "SUSPENDED", avatar: "S", places: 5 },
  { id: 4, name: "Giulia Rossi", email: "giulia@example.com", phone: "+39 347 8765432", experience: "6 years", expertise: "Food, Culture", verified: false, rating: 4.6, status: "ACTIVE", avatar: "G", places: 9 },
  { id: 5, name: "Ananya Iyer", email: "ananya@example.com", phone: "+91 98712 34567", experience: "4 years", expertise: "Heritage, Cultural", verified: true, rating: 4.9, status: "ACTIVE", avatar: "A", places: 7 },
  { id: 6, name: "Ravi Kumar", email: "ravi@example.com", phone: "+91 88765 43210", experience: "2 years", expertise: "Historical", verified: false, rating: 3.8, status: "SUSPENDED", avatar: "R", places: 3 },
  { id: 7, name: "Tenzin Norbu", email: "tenzin@example.com", phone: "+91 97654 32109", experience: "8 years", expertise: "Mountain, Adventure", verified: true, rating: 5.0, status: "ACTIVE", avatar: "T", places: 15 },
  { id: 8, name: "Kai Olsen", email: "kai@example.com", phone: "+47 98 12 34 56", experience: "5 years", expertise: "Nature, Diving", verified: true, rating: 4.7, status: "ACTIVE", avatar: "K", places: 6 },
];

export const MOCK_REPORTED_GUIDES = [
  { id: 1, guide: "Ravi Kumar", guideEmail: "ravi@example.com", reason: "Inappropriate behavior during tour", reports: 5, reportedBy: "Priya S., Marco B., Aisha K.", date: "2026-07-25", status: "UNDER_REVIEW", avatar: "R" },
  { id: 2, guide: "Sam Wilson", guideEmail: "sam@example.com", reason: "Misleading tour descriptions", reports: 3, reportedBy: "Takeshi Y., Sofia A.", date: "2026-07-22", status: "SUSPENDED", avatar: "S" },
  { id: 3, guide: "Unknown Guide", guideEmail: "unknown@example.com", reason: "Fake credentials and photos", reports: 8, reportedBy: "Multiple users", date: "2026-07-20", status: "UNDER_REVIEW", avatar: "U" },
  { id: 4, guide: "Vikram Nair", guideEmail: "vikram@example.com", reason: "Late cancellations without refund", reports: 2, reportedBy: "Carlos R., Amara O.", date: "2026-07-18", status: "IGNORED", avatar: "V" },
];

export const MOCK_ACTIVE_USERS = [
  { id: 1, name: "Priya Sharma", role: "TRAVELLER", email: "priya@example.com", lastSeen: "Just now", location: "Bangalore", avatar: "P", online: true },
  { id: 2, name: "Arjun Mehta", role: "LOCAL_GUIDE", email: "arjun@example.com", lastSeen: "2 mins ago", location: "Shimla", avatar: "A", online: true },
  { id: 3, name: "Marco Bianchi", role: "TRAVELLER", email: "marco@example.com", lastSeen: "5 mins ago", location: "Mumbai", avatar: "M", online: true },
  { id: 4, name: "Kenji Sato", role: "LOCAL_GUIDE", email: "kenji@example.com", lastSeen: "8 mins ago", location: "Jaipur", avatar: "K", online: false },
  { id: 5, name: "Sofia Andersen", role: "TRAVELLER", email: "sofia@example.com", lastSeen: "12 mins ago", location: "Pune", avatar: "S", online: false },
  { id: 6, name: "Tenzin Norbu", role: "LOCAL_GUIDE", email: "tenzin@example.com", lastSeen: "15 mins ago", location: "Leh", avatar: "T", online: false },
];

export const MOCK_ANALYTICS = {
  monthly: [
    { month: "Jan", registrations: 45, placesUploaded: 12, approved: 9, rejected: 3 },
    { month: "Feb", registrations: 62, placesUploaded: 18, approved: 14, rejected: 4 },
    { month: "Mar", registrations: 78, placesUploaded: 25, approved: 19, rejected: 6 },
    { month: "Apr", registrations: 95, placesUploaded: 32, approved: 26, rejected: 6 },
    { month: "May", registrations: 110, placesUploaded: 40, approved: 33, rejected: 7 },
    { month: "Jun", registrations: 132, placesUploaded: 48, approved: 40, rejected: 8 },
    { month: "Jul", registrations: 156, placesUploaded: 55, approved: 47, rejected: 8 },
  ],
  guideGrowth: [
    { month: "Jan", guides: 30 }, { month: "Feb", guides: 42 }, { month: "Mar", guides: 55 },
    { month: "Apr", guides: 70 }, { month: "May", guides: 88 }, { month: "Jun", guides: 110 }, { month: "Jul", guides: 130 },
  ],
  travellerGrowth: [
    { month: "Jan", travellers: 120 }, { month: "Feb", travellers: 180 }, { month: "Mar", travellers: 250 },
    { month: "Apr", travellers: 340 }, { month: "May", travellers: 490 }, { month: "Jun", travellers: 680 }, { month: "Jul", travellers: 850 },
  ],
  topCategories: [
    { name: "Nature", value: 35 }, { name: "Cultural", value: 25 }, { name: "Adventure", value: 20 },
    { name: "Food", value: 12 }, { name: "Historical", value: 8 },
  ],
  mostActiveGuides: [
    { name: "Tenzin Norbu", places: 15 }, { name: "Arjun Mehta", places: 12 }, { name: "Giulia Rossi", places: 9 },
    { name: "Ananya Iyer", places: 7 }, { name: "Kai Olsen", places: 6 },
  ],
};
