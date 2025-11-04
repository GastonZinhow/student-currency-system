"use client";

import React, { useEffect, useState } from "react";
import { motion } from "framer-motion";
import { fadeUp, staggerContainer } from "../../../components/motionVariants";
import CommonCard from "../../../components/commonCard";
import { format } from "date-fns";
import { useRouter } from "next/navigation";

export default function VerSuasVantagens() {
  const [partner, setPartner] = useState<any>({ name: "", rewards: [] });
  const [loading, setLoading] = useState(true);
  const [selectedCompany, setSelectedCompany] = useState<any>(null);
  const router = useRouter();


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
        setPartner({
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

  const toggleRewardStatus = async (id: number) => {
    const reward = partner.rewards.find((r: any) => r.id === id);
    if (!reward) return;

    const updatedAdvantage = {
      name: reward.name,
      description: reward.description,
      cost: reward.cost ?? 0,
      picture: reward.picture ?? "",
      quantity: reward.quantity,
      isActive: !reward.active,
      companyId: selectedCompany?.id,
    };

    try {
      const res = await fetch(`http://localhost:8080/advantages/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedAdvantage),
      });

      if (!res.ok) {
        throw new Error("Erro ao atualizar status");
      }

      setPartner((prev: any) => ({
        ...prev,
        rewards: prev.rewards.map((r: any) =>
          r.id === id ? { ...r, active: !r.active } : r
        ),
      }));
    } catch (err) {
      console.error(err);
    }
  };


  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerContainer}
      className="p-6 max-w-6xl mx-auto space-y-6"
    >
      <motion.header variants={fadeUp} className="mb-4">
        <h1 className="text-3xl font-bold">
          {loading ? "..." : partner?.name}
        </h1>
        <p className="text-sm text-gray-500">Vantagens cadastradas</p>
      </motion.header>
      <motion.div variants={fadeUp}>
        <button
          onClick={() => router.push("/EmpresaParceira/Dashboard")}
          className="px-4 py-2 mb-4 text-sm font-medium text-white bg-blue-600 rounded hover:bg-blue-700 transition"
        >
          ← Voltar ao Início
        </button>
      </motion.div>
      <motion.section variants={fadeUp}>
        <CommonCard
          title="Minhas Vantagens"
          subtitle="Histórico completo das vantagens"
        >
          {loading ? (
            <div className="text-sm text-gray-500 p-4">Carregando...</div>
          ) : partner.rewards.length === 0 ? (
            <div className="text-sm text-gray-500 p-4">
              Nenhuma vantagem cadastrada.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Nome
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Descrição
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Quantidade
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Data de cadastro
                    </th>
                    <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {partner.rewards.map((r: any) => (
                    <tr key={r.id} className="hover:bg-gray-50 transition">
                      <td className="px-6 py-4 text-sm text-gray-700">{r.name}</td>
                      <td className="px-6 py-4 text-sm text-gray-700">{r.description}</td>
                      <td className="px-6 py-4 text-sm text-gray-700">{r.quantity}</td>
                      <td className="px-6 py-4 text-sm text-gray-500">
                        {format(new Date(), "dd/MM/yyyy")}
                      </td>
                      <td className="px-6 py-4 text-center">
                        <motion.div
                          className={`w-12 h-6 flex items-center rounded-full p-1 cursor-pointer ${
                            r.active ? "bg-emerald-500" : "bg-gray-300"
                          }`}
                          onClick={() => toggleRewardStatus(r.id)}
                        >
                          <motion.div
                            layout
                            transition={{ type: "spring", stiffness: 700, damping: 30 }}
                            className={`w-4 h-4 bg-white rounded-full shadow-md ${
                              r.active ? "translate-x-6" : ""
                            }`}
                          />
                        </motion.div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </CommonCard>
      </motion.section>
    </motion.div>
  );
}
