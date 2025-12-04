import axios from "axios";

export const api = axios.create({
  baseURL: 'https://student-currency-system-production-d075.up.railway.app', 
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    if (token) {
    config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export const login = async (email: string, password: string) => {
  const response = await api.post("/login", { email, password });
  localStorage.setItem("token", response.data.token);
};

export default api;