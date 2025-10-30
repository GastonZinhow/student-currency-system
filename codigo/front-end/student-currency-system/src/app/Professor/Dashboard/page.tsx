"use client";

import React, { useEffect, useState } from "react";
import Link from "next/link";
import { motion } from "framer-motion";
import { fadeUp, staggerContainer } from "../../../components/motionVariants";
import CommonCard from "../../../components/commonCard";
import { Coins, List, Bell, Send, PiggyBank, LogOut } from "lucide-react";
import { format } from "date-fns";
import { authService } from "@/services/authService";
import { useRouter } from "next/navigation";

type Transaction = {
  id: number;
  description: string;
  amount: number;
  date: string;
  type: "SEND" | "RECEIVE";
};

type Notification = {
  id: number;
  message: string;
  date: string;
};

type ProfessorData = {
  id: number;
  name: string;
  balance: number;
  summary: { sentLast7Days: number; studentsRewarded: number };
  transactions: Transaction[];
  notifications: Notification[];
};

export default function ProfessorDashboard() {
  const [professor, setProfessor] = useState<ProfessorData>({
    id: 0,
    name: "",
    balance: 0,
    summary: { sentLast7Days: 0, studentsRewarded: 0 },
    transactions: [],
    notifications: [],
  });

  const [loading, setLoading] = useState(true);
  const [showNotifications, setShowNotifications] = useState(false);
  const router = useRouter();
  const user = authService.getUser();

  // Logout
  const handleLogout = () => {
    authService.logout();
    router.push("/auth/login");
  };

  useEffect(() => {
    const fetchProfessorData = async () => {
      const user = authService.getUser();
      console.log("User:", user);

      if (!user?.login) return;

      setLoading(true);
      try {
        // === Passo 1: Busca todos os professores ===
        const res = await fetch("http://localhost:8080/professors");
        if (!res.ok) throw new Error("Erro ao buscar professores");

        const professors = await res.json();

        // === Passo 2: Localiza o professor pelo login ===
        const foundProfessor = professors.find((p: any) => p.login === user.login);
        if (!foundProfessor) {
          console.error("Professor não encontrado");
          return;
        }

        // === Passo 3: Busca transações desse professor ===
        let transactionsData: any[] = [];
        try {
          const transactionsRes = await fetch(
            `http://localhost:8080/transactions/professor/${foundProfessor.id}`
          );
          if (transactionsRes.ok) transactionsData = await transactionsRes.json();
        } catch (err) {
          console.warn("Erro ao buscar transações:", err);
        }

        // === Passo 4: Monta os dados para o estado ===
        setProfessor({
          id: foundProfessor.id,
          name: foundProfessor.name,
          balance: foundProfessor.coins ?? 0,
          summary: {
            sentLast7Days: transactionsData
              .filter((t) => t.type === "SEND")
              .reduce((acc, t) => acc + t.amount, 0),
            studentsRewarded: transactionsData.filter((t) => t.type === "SEND").length,
          },
          transactions: transactionsData.map((t) => ({
            id: t.id,
            description: t.reason ?? "Transação",
            amount: t.amount,
            date: t.createdAt ?? new Date().toISOString(),
            type: t.type,
          })),
          notifications: [
            {
              id: 1,
              message: "Seu saldo foi atualizado pelo administrador.",
              date: new Date().toISOString(),
            },
          ],
        });
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchProfessorData();
  }, []);

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerContainer}
      className="space-y-6 p-6 max-w-6xl mx-auto relative"
    >
      {/* Header */}
      <motion.header variants={fadeUp} className="flex items-center justify-between relative">
        <div>
          <h1 className="text-3xl font-bold">Olá, {loading ? "..." : professor.name}</h1>
          <p className="text-sm text-gray-500">Gerencie suas moedas para os alunos</p>
        </div>

        <div className="flex items-center gap-4 relative">
          {/* Notificações */}
          <div className="relative">
            <button
              onClick={() => setShowNotifications((p) => !p)}
              aria-label="Ver notificações"
              className="relative p-2 rounded-full hover:bg-gray-100 transition"
            >
              <Bell size={22} />
              {professor.notifications.length > 0 && (
                <span className="absolute -top-0.5 -right-0.5 bg-red-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                  {professor.notifications.length}
                </span>
              )}
            </button>

            {showNotifications && (
              <motion.div
                initial={{ opacity: 0, y: -5, scale: 0.98 }}
                animate={{ opacity: 1, y: 0, scale: 1 }}
                transition={{ duration: 0.18 }}
                className="absolute right-0 mt-2 w-80 bg-white border rounded-xl shadow-lg z-50 overflow-hidden"
              >
                <div className="p-3 border-b font-semibold">Notificações</div>
                <div className="max-h-60 overflow-y-auto divide-y">
                  {professor.notifications.map((n) => (
                    <div key={n.id} className="p-3 text-sm hover:bg-gray-50">
                      <div>{n.message}</div>
                      <div className="text-xs text-gray-500 mt-1">
                        {format(new Date(n.date), "dd/MM/yyyy HH:mm")}
                      </div>
                    </div>
                  ))}
                </div>
              </motion.div>
            )}
          </div>

          {/* Botões */}
          <Link href="/Professor/EnviarMoedas">
            <button className="px-4 py-2 rounded-lg bg-amber-500 text-white hover:bg-amber-600 transition flex items-center gap-2">
              <Send size={16} /> Enviar Moedas
            </button>
          </Link>

          <button
            onClick={handleLogout}
            className="px-4 py-2 rounded-lg bg-red-500 text-white hover:bg-red-600 transition flex items-center gap-2"
          >
            <LogOut size={16} /> Sair
          </button>
        </div>
      </motion.header>

      {/* Cards */}
      <motion.section variants={fadeUp} className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <CommonCard title="Saldo Disponível" subtitle="Moedas para enviar aos alunos">
          <div className="flex items-center gap-6">
            <div className="flex items-center gap-3">
              <div className="p-4 rounded-xl bg-gray-100">
                <PiggyBank size={38} />
              </div>
              <div>
                <div className="text-4xl font-semibold">
                  {loading ? "..." : professor.balance}
                </div>
                <div className="text-sm text-gray-500">moedas</div>
              </div>
            </div>
          </div>
        </CommonCard>

        <CommonCard title="Resumo rápido" subtitle="Últimos 7 dias">
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <div>
                <div className="text-sm text-gray-500">Moedas enviadas</div>
                <div className="font-semibold">
                  {loading ? "..." : professor.summary.sentLast7Days} moedas
                </div>
              </div>
              <div>
                <div className="text-sm text-gray-500">Alunos beneficiados</div>
                <div className="font-semibold">
                  {loading ? "..." : professor.summary.studentsRewarded}
                </div>
              </div>
            </div>
          </div>
        </CommonCard>
      </motion.section>

      {/* Histórico */}
      <motion.section variants={fadeUp}>
        <CommonCard title="Histórico de envios">
          <div className="mt-3 space-y-2">
            {loading ? (
              <div className="text-sm text-gray-500">Carregando...</div>
            ) : professor.transactions.length === 0 ? (
              <div className="text-sm text-gray-500">Nenhuma transação encontrada</div>
            ) : (
              professor.transactions.map((t) => (
                <div
                  key={t.id}
                  className="flex items-center justify-between p-3 rounded-lg hover:bg-gray-50 transition"
                >
                  <div className="flex items-center gap-3">
                    <div className="p-2 rounded-md bg-gray-100">
                      <List size={18} />
                    </div>
                    <div>
                      <div className="font-medium">{t.description}</div>
                      <div className="text-xs text-gray-500">
                        {format(new Date(t.date), "dd/MM/yyyy HH:mm")}
                      </div>
                    </div>
                  </div>
                  <div
                    className={`font-semibold ${
                      t.type === "SEND" ? "text-rose-600" : "text-green-600"
                    }`}
                  >
                    {t.type === "SEND" ? `-${t.amount}` : `+${t.amount}`}
                  </div>
                </div>
              ))
            )}
          </div>
        </CommonCard>
      </motion.section>
    </motion.div>
  );
}
