import React, { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'

const AdminRoute = ({ setIsLoading, children }) => {
    const [isAdmin, setIsAdmin] = useState(false);

    const checkAdmin = async () => {
        try {
            const resp = await fetch("http://localhost:8080/api/admin", {
                
            })
        } catch (error) {
            console.log("Failed to Check Admin Rights", error);
            setIsAdmin(false);
            setIsLoading(false);
        }
    }

    useEffect(() => {
        checkAdmin();
    }, []);

    return (
        isAdmin ? children : <Navigate to="/dashboard" replace />
    )
}

export default AdminRoute