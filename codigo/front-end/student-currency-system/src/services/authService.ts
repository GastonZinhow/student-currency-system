import { api } from "./api";

export const authService = {
  login: async (login: string, password: string) => {
    const { data } = await api.post("/auth/login", { login, password });

    localStorage.setItem("user", JSON.stringify(data));
    localStorage.setItem("role", data.role);
    localStorage.setItem("userId", data.userId);
    localStorage.setItem("login", data.login);

    return data;
  },

  register: async (formData: any) => {
    const { data } = await api.post("/students", formData);
    return data;
  },

  registerCompany: async (formData: any) => {
    const { data } = await api.post("/companies", formData);
    return data;
  },

  logout: () => {
    localStorage.removeItem("user");
    localStorage.removeItem("role");
    localStorage.removeItem("id");
    localStorage.removeItem("login");
  },

  getUser: () => {
    if (typeof window === "undefined") return null;
    const user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null;
  },

  updateUser: (updatedUser: any) => {
    if (!updatedUser) return;

    localStorage.setItem("user", JSON.stringify(updatedUser));

    if (updatedUser.role) {
      localStorage.setItem("role", updatedUser.role);
    }
    if (updatedUser.login) {
      localStorage.setItem("login", updatedUser.login);
    }

    return updatedUser;
    },

  getRole: () => {
    if (typeof window === "undefined") return null;
    return localStorage.getItem("role");
  },

};
