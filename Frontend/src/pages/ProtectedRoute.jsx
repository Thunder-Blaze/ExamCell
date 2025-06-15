import React, { useEffect, useState } from 'react'
import toast from 'react-hot-toast';
import { Navigate } from 'react-router-dom'

const ProtectedRoute = ({ children }) => {
  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [hasDetails, setHasDetails] = useState(false);

  const hasDetailsFunc = async (email) => {
    const ParsedEmail = email.replaceAll("\"","");
    try {
      const resp = await fetch(`http://localhost:8080/api/students/${ParsedEmail}`, {
        method: 'GET',
      });
      console.log(resp)
      if (resp.ok) {
        setHasDetails(true);
        const studentData = await resp.json();
        localStorage.setItem("studentData", JSON.stringify(studentData));
        return;
      }
      setHasDetails(false)
    } catch (error) {
      console.log(error);
      setHasDetails(false)
    }
  }

  const handleValidAuth = async (email) => {
    setIsAuthenticated(true);
    toast.success("Authenticated Visit")
    await hasDetailsFunc(email);
    setIsLoading(false);
  }

  const handleInvalidAuth = () => {
    sessionStorage.setItem("token", "");
    sessionStorage.setItem("email", "");
    setIsAuthenticated(false);
    toast.error("Not Authorized")
    setIsLoading(false);
  }

  const validateToken = async () => {
      let token = sessionStorage.getItem("token")
      let email = sessionStorage.getItem("email")
      if (token && email) {
        try {
          let resp = await fetch('http://localhost:8080/api/validate-token', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify({ token, email }),
          });
          // console.log(resp)
          if (resp.ok) {
            handleValidAuth(email)
            return;
          }
          handleInvalidAuth()
        } catch (err) {
          console.log(err);
          handleInvalidAuth();
        }
      } else {
        handleInvalidAuth();
      }
    }

  useEffect(() => {
    validateToken()
  }, [])
  
  return !isLoading && (
    isAuthenticated ? (
      hasDetails ? children : <Navigate to="/inputform" replace />
    ) : (
      <Navigate to="/" replace />
    )
  )
}

export default ProtectedRoute