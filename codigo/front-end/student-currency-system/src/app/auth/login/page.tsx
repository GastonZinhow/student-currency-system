"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { authService } from "@/services/authService";
import { LogIn, Mail, Lock, Bitcoin } from "lucide-react";

export default function LoginPage() {
  const router = useRouter();
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const user = await authService.login(login, password);

      if (!user) {
        setError("Login ou senha incorretos.");
        return;
      }

      if (user.role === "STUDENT") {
        router.replace("/Aluno/Dashboard");
      } else if (user.role === "PROFESSOR") {
        router.replace("/Professor/Dashboard");
      } else if (user.role === "COMPANY") {
        router.replace("/EmpresaParceira/Dashboard");
      } else {
        router.replace("/dashboard");
      }
    } catch {
      setError("Login ou senha incorretos.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex h-screen bg-neutral-900 text-white">

      {/* LEFT IMAGE AREA */}
      <div className="hidden lg:flex w-1/2 items-center justify-center bg-neutral-950 relative">
        <img
          src="https://img.freepik.com/vetores-premium/bitcoin-em-estilo-pixel-art_735839-2149.jpg"
          alt="Moedas"
          className="w-full h-full object-cover opacity-60"
        />

        {/* LOGO EM CIMA DA IMAGEM */}
        <div className="absolute top-10 left-10 flex items-center gap-3">
          <h1 className="text-3xl font-bold">Academic Moeda</h1>
          <Bitcoin size={40} className="text-yellow-400" />
        </div>

        {/* TEXTO ESTILO LANDING */}
        <div className="absolute bottom-20 left-10 max-w-lg">
          <h2 className="text-4xl font-bold mb-4 text-shadow-lg">Transforme suas Conquistas em Recompensas</h2>
          <p className=" text-lg text-shadow-lg">
            Ganhe moedas acadêmicas, resgate vantagens e conquiste reconhecimento.
          </p>
        </div>
      </div>

      {/* RIGHT LOGIN CARD */}
      <div className="flex w-full lg:w-1/2 items-center justify-center">
        <div className="w-full max-w-sm rounded-2xl bg-neutral-800 p-8 shadow-2xl">

          <div className="flex items-center justify-center gap-2 mb-6">
            <LogIn size={28} />
            <h1 className="text-2xl font-semibold">Acessar conta</h1>
          </div>

          <form onSubmit={handleLogin} className="flex flex-col gap-4">
            <div>
              <label className="text-sm text-gray-300">Login</label>
              <div className="flex items-center gap-2 rounded-lg bg-neutral-700 px-3 py-2">
                <Mail size={18} className="text-gray-400" />
                <input
                  type="text"
                  className="w-full bg-transparent outline-none text-sm text-white placeholder-gray-400"
                  placeholder="Digite seu login"
                  value={login}
                  onChange={(e) => setLogin(e.target.value)}
                />
              </div>
            </div>

            <div>
              <label className="text-sm text-gray-300">Senha</label>
              <div className="flex items-center gap-2 rounded-lg bg-neutral-700 px-3 py-2">
                <Lock size={18} className="text-gray-400" />
                <input
                  type="password"
                  className="w-full bg-transparent outline-none text-sm text-white placeholder-gray-400"
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
            </div>

            {error && <p className="text-red-400 text-sm">{error}</p>}

            <button
              type="submit"
              disabled={loading}
              className="mt-2 rounded-lg bg-blue-600 py-2 font-medium hover:bg-blue-700 transition disabled:opacity-50"
            >
              {loading ? "Entrando..." : "Entrar"}
            </button>

            <p className="text-sm text-gray-400 text-center mt-2">
              Não tem uma conta?{" "}
              <span
                className="text-green-400 hover:underline cursor-pointer"
                onClick={() => router.push("/auth/register")}
              >
                Criar conta
              </span>
            </p>
          </form>
        </div>
      </div>

    </div>
  );
}
