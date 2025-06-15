import React from 'react'
import toast from 'react-hot-toast'
import { Navigate } from 'react-router-dom'

const Logout = () => {

    sessionStorage.removeItem("token")
    sessionStorage.removeItem("email")
    localStorage.removeItem("userData")
    toast.success("Logged Out Successfully")

    return (
        <Navigate to="/" replace />
    )
}

export default Logout