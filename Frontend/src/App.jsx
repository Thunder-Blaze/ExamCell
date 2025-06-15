import React, { useState } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { ThemeProvider } from "./components/theme-provider";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import AdminDashboard from "./pages/DashboardPage"
import ManageUsersPage from "./pages/ManageUsersPage"
import CertificateRequestsPage from "./pages/CertificateRequestsPage"
import TemplatesPage from "./pages/TemplatesPage"
import GeneratedCertificatesPage from "./pages/GeneratedCertificatesPage"
import History from "./pages/History";
import InputForm from "./pages/InputForm";
import ProtectedRoute from "./pages/ProtectedRoute";
import { Toaster } from 'react-hot-toast'
import AdminRoute from "./pages/AdminRoute";

const App = () => {
  const [isLoading, setIsLoading] = useState(true);

  return (
    <ThemeProvider defaultTheme="system" storageKey="exam-cell-theme">
      <Toaster reverseOrder={false} position="top-right" />
      <BrowserRouter>
        { !isLoading &&
          (
            <Routes>
              <Route path="/" element={<Login />} />
              <Route path="/dashboard" element={
                <ProtectedRoute setIsLoading={setIsLoading}>
                  <Dashboard />
                </ProtectedRoute>
              } />
              <Route path="/admin-dashboard" element={
                <AdminRoute setIsLoading={setIsLoading}>
                  <AdminDashboard />
                </AdminRoute>
              } />
              <Route path="/manage-users" element={
                <AdminRoute setIsLoading={setIsLoading}>
                  <ManageUsersPage />
                </AdminRoute>
              } />
              <Route path="/certificate-requests" element={
                <AdminRoute setIsLoading={setIsLoading}>
                  <CertificateRequestsPage />
                </AdminRoute>
              } />
              <Route path="/templates" element={
                <AdminRoute setIsLoading={setIsLoading}>
                  <TemplatesPage />
                </AdminRoute>
              } />
              <Route path="/generated-certificates" element={
                <AdminRoute setIsLoading={setIsLoading}>
                  <GeneratedCertificatesPage />
                </AdminRoute>
              } />
              <Route path="/history" element={
                <AdminRoute setIsLoading={setIsLoading}>
                  <History />
                </AdminRoute>
              } />
              <Route path="/inputform" element={
                <ProtectedRoute setIsLoading={setIsLoading}>
                  <InputForm />
                </ProtectedRoute>
              } />
            </Routes>
          )
        }
      </BrowserRouter>
    </ThemeProvider>
  );
};

export default App;
