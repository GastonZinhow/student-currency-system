"use client";

import React, { useEffect, useState } from "react";
import Link from "next/link";
import CommonCard from "../../../components/commonCard";
import { partner as partnerMock } from "../../../utils/mockData";
import { fadeUp, staggerContainer } from "../../../components/motionVariants";
import { motion } from "framer-motion";
import { Bell, Gift, LogOut, Repeat } from "lucide-react";
import { format } from "date-fns";
import { authService } from "@/services/authService";
import { useRouter } from "next/navigation";

export default function PartnerDashboard() {
  const [partner, setPartner] = useState<any>(null);
  const [advantages, setAdvantages] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [showNotifications, setShowNotifications] = useState(false);
  const [selectedCompany, setSelectedCompany] = useState<any>(null);
  const user = authService.getUser();
  const router = useRouter();
  
  const handleLogout = () => {
    authService.logout();
    router.push("/auth/login");
  };

  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user") || "{}");

    if (user?.login) {
      fetch("http://localhost:8080/companies")
        .then((res) => res.json())
        .then((data) => {
          const comp = data.find((c: any) => c.login === user.login);
          if (comp) setSelectedCompany(comp);
        })
        .catch((err) => console.error("Erro ao buscar empresa:", err));
    }
  }, []);

  useEffect(() => {
    if (!selectedCompany?.id) return;

    fetch(`http://localhost:8080/advantages/company/${selectedCompany.id}`)
      .then((res) => res.json())
      .then((data) => {
        setAdvantages({
          name: selectedCompany.name,
          rewards: data.map((adv: any) => ({
            id: adv.id,
            name: adv.name,
            description: adv.description,
            quantity: adv.quantity,
            date: new Date(),
            active: adv.isActive,
          })),
        });
        setLoading(false);
      })
      .catch((err) => console.error("Erro ao carregar vantagens:", err));
  }, [selectedCompany]);

  useEffect(() => {
    const t = setTimeout(() => {
      setPartner({
        ...partnerMock,
        notifications: [
          { id: 1, message: "Nova troca confirmada: João Silva", date: "2025-10-14T10:30" },
          { id: 2, message: "Você recebeu uma nova avaliação positiva.", date: "2025-10-15T12:15" },
          { id: 3, message: "Vantagem 'Desconto 10%' foi resgatada.", date: "2025-10-15T15:00" },
        ],
      });
      setLoading(false);
    }, 380);
    return () => clearTimeout(t);
  }, []);

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerContainer}
      className="p-6 max-w-6xl mx-auto space-y-6 relative"
    >
      {/* Header */}
      <motion.header variants={fadeUp} className="flex items-center justify-between relative">
        <div>
          <h1 className="text-3xl font-bold">{loading ? "..." : user.login}</h1>
          <p className="text-sm text-gray-500">Painel da empresa parceira</p>
        </div>

        <div className="flex items-center gap-4 relative">
          {/* Notificações */}
          <div className="relative">
            <button
              onClick={() => setShowNotifications((prev) => !prev)}
              className="relative p-2 rounded-full hover:bg-gray-100 transition"
            >
              <Bell size={22} />
              {partner?.notifications?.length > 0 && (
                <span className="absolute -top-0.5 -right-0.5 bg-red-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                  {partner.notifications.length}
                </span>
              )}
            </button>

            {showNotifications && (
              <motion.div
                initial={{ opacity: 0, y: -5 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.2 }}
                className="absolute right-0 mt-2 w-80 bg-white border rounded-xl shadow-lg z-50 overflow-hidden"
              >
                <div className="p-3 border-b font-semibold">Notificações</div>
                <div className="max-h-60 overflow-y-auto divide-y">
                  {partner?.notifications.map((n: any) => (
                    <div key={n.id} className="p-3 text-sm hover:bg-gray-50">
                      <div>{n.message}</div>
                      <div className="text-xs text-gray-500 mt-1">
                        {format(new Date(n.date), "dd/MM/yyyy HH:mm")}
                      </div>
                    </div>
                  ))}
                </div>
                <div className="text-center text-xs text-gray-500 py-2 bg-gray-50 cursor-pointer hover:text-blue-600">
                  Ver todas
                </div>
                
              </motion.div>
            )}
          </div>

          {/* Botão Cadastrar Vantagem */}
          <Link href="/EmpresaParceira/CadastrarVantagem" className="px-4 py-2 rounded-lg bg-blue-500 text-white hover:bg-blue-600 transition">
            Cadastrar Vantagem
          </Link>
          <button
            onClick={handleLogout}
            className="px-4 py-2 rounded-lg bg-red-500 text-white hover:bg-red-600 transition flex items-center gap-2"
            aria-label="Sair da conta"
          >
            <LogOut size={16} /> Sair
          </button>
        </div>
      </motion.header>

      {/* Cards principais */}
      <motion.section variants={fadeUp} className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <CommonCard title="Vantagens cadastradas" subtitle="Total de ofertas">
          <div className="flex items-center gap-6">
            <div className="flex items-center gap-3">
              <div className="p-4 rounded-xl bg-gray-100">
                <Gift size={38} />
              </div>
              <div className="text-4xl font-semibold">{loading ? "..." : advantages.rewards.length}</div>
            </div>

            <div className="ml-auto">
              <Link
                href="/EmpresaParceira/VerSuasVantagens"
                className="px-4 py-2 rounded-lg bg-emerald-500 text-white hover:scale-[1.02] transition-all flex items-center gap-2"
              >
                <Gift size={18} /> Ver Suas Vantagens
              </Link>
            </div>
          </div>
        </CommonCard>

        <CommonCard title="Trocas realizadas" subtitle="Total histórico">
          <div className="flex items-center gap-6">
            <div className="flex items-center gap-3">
              <div className="p-4 rounded-xl bg-gray-100">
                <Repeat size={38} />
              </div>
              <div className="text-4xl font-semibold">{loading ? "..." : partner?.totalExchanges}</div>
            </div>

            <div className="ml-auto">
              <Link
                href="/EmpresaParceira/ConsultarTrocas"
                className="px-4 py-2 rounded-lg bg-blue-500 text-white hover:scale-[1.02] transition-all flex items-center gap-2"
              >
                <Repeat size={18} /> Consultar Trocas
              </Link>
            </div>
          </div>
        </CommonCard>
      </motion.section>

      {/* Últimas trocas */}
      <motion.section variants={fadeUp} className="grid grid-cols-1 gap-6">
        <CommonCard title="Visão rápida" subtitle="Últimas trocas">
          <div className="divide-y mt-2">
            {loading ? (
              <div className="text-sm text-gray-500">Carregando...</div>
            ) : (
              partner?.exchangesRecent.map((e: any) => (
                <div key={e.id} className="py-3 flex items-center justify-between">
                  <div>
                    <div className="font-medium">
                      {e.student} — <span className="text-gray-500">{e.reward}</span>
                    </div>
                    <div className="text-xs text-gray-500">{new Date(e.date).toLocaleString()}</div>
                  </div>
                  <div>
                    <span
                      className={`px-3 py-1 rounded-full text-xs ${
                        e.status === "Confirmado"
                          ? "bg-emerald-100 text-emerald-700"
                          : "bg-yellow-100 text-yellow-700"
                      }`}
                    >
                      {e.status}
                    </span>
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
