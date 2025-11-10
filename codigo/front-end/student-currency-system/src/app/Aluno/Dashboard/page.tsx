"use client";

import React, { useEffect, useState } from "react";
import Link from "next/link";
import { motion } from "framer-motion";
import { fadeUp, staggerContainer } from "@/components/motionVariants";
import CommonCard from "../../../components/commonCard";
import { Coins, List, Bell, Gift, PiggyBank, LogOut } from "lucide-react";
import { format } from "date-fns";
import { authService } from "@/services/authService";
import { useRouter } from "next/navigation";
import api from "@/services/api";
import { StudentDTO } from "@/utils/studentData";
import { TransactionDTO } from "@/utils/transactionData";
import { RedemptionDTO } from "@/utils/redemptionData";

const mockNotifications = [
  {
    id: 1,
    message: "Você recebeu 20 moedas do Prof. Carlos!",
    date: "2025-10-10T14:00",
  },
  {
    id: 2,
    message: "Sua troca por 'Caneca personalizada' foi confirmada.",
    date: "2025-10-12T09:45",
  },
  {
    id: 3,
    message: "Você ganhou 10 moedas por participação em aula.",
    date: "2025-10-13T17:15",
  },
];

export default function StudentDashboard() {
  const [student, setStudent] = useState<StudentDTO>({
    id: 0,
    name: "",
    email: "",
    cpf: "",
    rg: "",
    address: "",
    course: "",
    coins: 0,
    instituition: { id: 0, name: "" },
  });

  const [transactions, setTransactions] = useState<TransactionDTO[]>([]);
  const [redemptions, setRedemptions] = useState<RedemptionDTO[]>([]);
  const [summary, setSummary] = useState({ last7DaysGained: 0, exchanges: 0 });
  const [loading, setLoading] = useState(true);
  const [showNotifications, setShowNotifications] = useState(false);

  const router = useRouter();
  const user = authService.getUser();

  const handleLogout = () => {
    authService.logout();
    router.push("/auth/login");
  };

  const fetchStudentData = async () => {
    if (!user?.userId) return;

    setLoading(true);
    try {
      const { data: studentData } = await api.get<StudentDTO>(
        `/students/${user.userId}`
      );

      const { data: transactionsData } = await api.get<TransactionDTO[]>(
        `/transactions/student/${user.userId}`
      );

      const { data: redemptionsData } = await api.get<RedemptionDTO[]>(
        `/redemptions/student/${user.userId}`
      );

      const now = new Date();
      const last7DaysGained = transactionsData
        .filter(
          (t) =>
            new Date(t.createdAt) >=
            new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
        )
        .reduce((acc, t) => acc + t.amount, 0);

      const exchanges = redemptionsData.length;

      setStudent(studentData);
      setTransactions(transactionsData);
      setRedemptions(redemptionsData);
      setSummary({ last7DaysGained, exchanges });
    } catch (error) {
      console.error("Erro ao buscar dados do aluno:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStudentData();

    const handleFocus = () => {
      fetchStudentData();
    };

    window.addEventListener("focus", handleFocus);

    return () => {
      window.removeEventListener("focus", handleFocus);
    };
  }, [user?.userId]);

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerContainer}
      className="space-y-6 p-6 max-w-6xl mx-auto relative"
    >
      {/* Cabeçalho */}
      <motion.header
        variants={fadeUp}
        className="flex items-center justify-between relative"
      >
        <div>
          <h1 className="text-3xl font-bold">
            Olá, {loading ? "..." : student.name}
          </h1>
          <p className="text-sm text-gray-500">
            Bem-vindo ao seu painel de moedas
          </p>
        </div>

        <div className="flex items-center gap-4 relative">
          {/* Notificações */}
          <div className="relative">
            <button
              onClick={() => setShowNotifications((prev) => !prev)}
              aria-label="Ver notificações"
              className="relative p-2 rounded-full hover:bg-gray-100 transition focus:outline-none focus:ring-2 focus:ring-blue-200"
            >
              <Bell size={22} />
              {mockNotifications.length > 0 && (
                <span className="absolute -top-0.5 -right-0.5 bg-red-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                  {mockNotifications.length}
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
                  {mockNotifications.map((n) => (
                    <div key={n.id} className="p-3 text-sm hover:bg-gray-50">
                      <div>{n.message}</div>
                      <div className="text-xs text-gray-500 mt-1">
                        {n.date && format(new Date(n.date), "dd/MM/yyyy HH:mm")}
                      </div>
                    </div>
                  ))}
                </div>
              </motion.div>
            )}
          </div>

          {/* Trocar Moedas */}
          <Link href="/Aluno/TrocarMoedas">
            <button className="px-4 py-2 rounded-lg bg-emerald-500 text-white hover:bg-emerald-600 transition flex items-center gap-2">
              <Gift size={16} /> Trocar moedas
            </button>
          </Link>

          {/* Logout */}
          <button
            onClick={handleLogout}
            className="px-4 py-2 rounded-lg bg-red-500 text-white hover:bg-red-600 transition flex items-center gap-2"
          >
            <LogOut size={16} /> Sair
          </button>
        </div>
      </motion.header>

      {/* Cards principais */}
      <motion.section
        variants={fadeUp}
        className="grid grid-cols-1 md:grid-cols-2 gap-6"
      >
        <CommonCard
          title="Saldo de Moedas"
          subtitle="Valor disponível para trocas"
        >
          <div className="flex items-center gap-6">
            <div className="flex items-center gap-3">
              <div className="p-4 rounded-xl bg-gray-100">
                <PiggyBank size={38} />
              </div>
              <div>
                <div className="text-4xl font-semibold">
                  {loading ? "..." : student.coins}
                </div>
                <div className="text-sm text-gray-500">moedas</div>
              </div>
            </div>

            <div className="ml-auto">
              <Link href="/Aluno/ConsultarExtrato">
                <button className="px-4 py-2 rounded-lg bg-blue-500 text-white hover:scale-[1.02] transition-all flex items-center gap-2">
                  Consultar extrato
                </button>
              </Link>
            </div>
          </div>
        </CommonCard>

        <CommonCard title="Resumo rápido" subtitle="Últimos 7 dias">
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <div>
                <div className="text-sm text-gray-500">
                  Ganho últimos 7 dias
                </div>
                <div className="font-semibold">
                  {loading ? "..." : summary.last7DaysGained} moedas
                </div>
              </div>
              <div>
                <div className="text-sm text-gray-500">Total de trocas</div>
                <div className="font-semibold">
                  {loading ? "..." : redemptions?.length}
                </div>
              </div>
            </div>
            <div className="mt-2 bg-gray-50 p-3 rounded-lg">
              <div className="h-2 bg-gray-200 rounded-full overflow-hidden">
                <div
                  className="h-full rounded-full bg-blue-500"
                  style={{
                    width: loading
                      ? "0%"
                      : `${Math.min(
                          100,
                          (summary.last7DaysGained / 150) * 100
                        )}%`,
                  }}
                />
              </div>
            </div>
          </div>
        </CommonCard>
      </motion.section>

      {/* Transações recentes */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <motion.section variants={fadeUp}>
          <CommonCard
            title="Transações recentes"
            actions={<div className="text-sm text-gray-500">Últimas 8</div>}
          >
            <div className="mt-3 space-y-2">
              {loading ? (
                <div className="text-sm text-gray-500">Carregando...</div>
              ) : (
                transactions.slice(0, 6).map((t: TransactionDTO) => (
                  <div
                    key={t.id}
                    className="flex items-center justify-between p-3 rounded-lg hover:bg-gray-50 transition"
                  >
                    <div className="flex items-center gap-3">
                      <div className="p-2 rounded-md bg-gray-100">
                        <List size={18} />
                      </div>
                      <div>
                        <div className="font-medium">{t.reason}</div>
                        <div className="text-xs text-gray-500">
                          {format(new Date(t.createdAt), "dd/MM/yyyy HH:mm")}
                        </div>
                      </div>
                    </div>
                    <div className="font-semibold text-emerald-600">
                      +{t.amount}
                    </div>
                  </div>
                ))
              )}
            </div>
          </CommonCard>
        </motion.section>
        {/* Resgates recentes */}
        <motion.section variants={fadeUp}>
          <CommonCard
            title="Resgates recentes"
            actions={<div className="text-sm text-gray-500">Últimos 4</div>}
          >
            <div className="mt-3 space-y-2">
              {loading ? (
                <div className="text-sm text-gray-500">Carregando...</div>
              ) : (
                redemptions.slice(0, 4).map((r: RedemptionDTO) => (
                  <div
                    key={r.id}
                    className="flex items-center justify-between p-3 rounded-lg hover:bg-gray-50 transition"
                  >
                    <div className="flex items-center gap-3">
                      <div className="p-2 rounded-md bg-red-100">
                        <List size={18} />
                      </div>
                      <div>
                        <div className="font-medium">{r.advantage.name}</div>
                        <div className="text-xs text-gray-500">
                          {r.createdAt &&
                            format(new Date(r.createdAt), "dd/MM/yyyy HH:mm")}
                        </div>
                      </div>
                    </div>
                    <div className="font-semibold text-red-600">
                      -{r.advantage.cost}
                    </div>
                  </div>
                ))
              )}
            </div>
          </CommonCard>
        </motion.section>
      </div>
    </motion.div>
  );
}
