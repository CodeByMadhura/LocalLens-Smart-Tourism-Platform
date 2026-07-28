import React, { createContext, useContext, useState, useEffect } from "react";

// Create the context
const AuthContext = createContext(null);

// AuthProvider wraps the entire app to provide auth state everywhere
export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [authToken, setAuthToken] = useState(null);
  const [loading, setLoading] = useState(true); // check localStorage on startup

  // On app load, restore auth from localStorage
  useEffect(() => {
    const savedToken = localStorage.getItem("authToken");
    const savedUser = localStorage.getItem("currentUser");

    if (savedToken && savedUser) {
      try {
        setAuthToken(savedToken);
        setCurrentUser(JSON.parse(savedUser));
      } catch (e) {
        // If parsing fails, clear storage
        localStorage.removeItem("authToken");
        localStorage.removeItem("currentUser");
      }
    }
    setLoading(false);
  }, []);

  // Called after successful login
  const login = (userData, token) => {
    localStorage.setItem("authToken", token);
    localStorage.setItem("currentUser", JSON.stringify(userData));
    setAuthToken(token);
    setCurrentUser(userData);
  };

  // Called on logout
  const logout = () => {
    localStorage.removeItem("authToken");
    localStorage.removeItem("currentUser");
    setAuthToken(null);
    setCurrentUser(null);
  };

  // Helper: is user logged in?
  const isLoggedIn = !!currentUser && !!authToken;

  // Helper: is user a local guide?
  const isLocalGuide = currentUser?.role === "LOCAL_GUIDE";

  // Helper: is user a traveller?
  const isTraveller = currentUser?.role === "TRAVELLER";

  // Helper: is user an admin?
  const isAdmin = currentUser?.role === "ADMIN";

  return (
    <AuthContext.Provider value={{ currentUser, authToken, isLoggedIn, isLocalGuide, isTraveller, isAdmin, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  );
}

// Custom hook for easy use in any component
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
