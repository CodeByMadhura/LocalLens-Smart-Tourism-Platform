function Login() {
  const styles = {
    container: {
      height: "100vh",
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      background: "linear-gradient(135deg, #4f46e5, #7c3aed)",
      fontFamily: "Arial, sans-serif",
    },
    card: {
      width: "350px",
      background: "#fff",
      padding: "35px",
      borderRadius: "12px",
      boxShadow: "0 10px 25px rgba(0,0,0,0.2)",
      textAlign: "center",
    },
    heading: {
      marginBottom: "8px",
      color: "#333",
    },
    text: {
      color: "#666",
      marginBottom: "25px",
    },
    input: {
      width: "100%",
      padding: "12px",
      marginBottom: "15px",
      border: "1px solid #ccc",
      borderRadius: "8px",
      fontSize: "15px",
      outline: "none",
      boxSizing: "border-box",
    },
    button: {
      width: "100%",
      padding: "12px",
      backgroundColor: "#4f46e5",
      color: "#fff",
      border: "none",
      borderRadius: "8px",
      fontSize: "16px",
      cursor: "pointer",
      marginTop: "10px",
    },
    link: {
      display: "block",
      marginTop: "15px",
      color: "#4f46e5",
      textDecoration: "none",
      fontSize: "14px",
    },
    signup: {
      marginTop: "20px",
      fontSize: "14px",
      color: "#555",
    },
    signupLink: {
      color: "#4f46e5",
      textDecoration: "none",
      fontWeight: "bold",
    },
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.heading}>Welcome Back</h2>
        <p style={styles.text}>Sign in to continue</p>

        <input
          type="email"
          placeholder="Enter your email"
          style={styles.input}
        />

        <input
          type="password"
          placeholder="Enter your password"
          style={styles.input}
        />

        <button style={styles.button}>Login</button>

        <a href="/" style={styles.link}>
          Forgot Password?
        </a>

        <p style={styles.signup}>
          Don't have an account?{" "}
          <a href="/" style={styles.signupLink}>
            Sign Up
          </a>
        </p>
      </div>
    </div>
  );
}

export default Login;