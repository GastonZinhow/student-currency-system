"use client";

import React, { useState, useEffect } from "react";
import { motion } from "framer-motion";
import { fadeUp, staggerContainer } from "@/components/motionVariants";
import { X } from "lucide-react";
import Link from "next/link";

const SendCoins = () => {
  const [students, setStudents] = useState<any[]>([]);
  const [selectedStudent, setSelectedStudent] = useState<any>(null);
  const [amount, setAmount] = useState<number>(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [professor, setProfessor] = useState<any>(null);

  // 🟢 Buscar dados do professor logado
  useEffect(() => {
    const user = JSON.parse(localStorage.getItem("user") || "{}");

    if (user?.login) {
      fetch("http://localhost:8080/professors")
        .then((res) => res.json())
        .then((data) => {
          const prof = data.find((p: any) => p.login === user.login);
          if (prof) setProfessor(prof);
        })
        .catch((err) => console.error("Erro ao buscar professor:", err));
    }
  }, []);

  // 🟢 Buscar alunos ao carregar a página
  useEffect(() => {
    fetch("http://localhost:8080/students")
      .then((res) => res.json())
      .then((data) => setStudents(data))
      .catch((err) => console.error("Erro ao carregar alunos:", err));
  }, []);

  const openModal = (student: any) => {
    setSelectedStudent(student);
    setAmount(0);
    setModalOpen(true);
  };

  // 🟢 Enviar moedas (usa professorId dinâmico)
  const sendCoins = async () => {
    if (!selectedStudent || !professor) {
      alert("❌ Professor não identificado. Faça login novamente.");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/transactions/send", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          professorId: professor.id,
          studentId: selectedStudent.id,
          amount,
          reason: "Recompensa de participação",
        }),
      });

      if (response.ok) {
        alert(`✅ Você enviou ${amount} moedas para ${selectedStudent.name}`);
        setModalOpen(false);
        setAmount(0);

        // Atualiza saldos localmente
        setStudents((prev) =>
          prev.map((s) =>
            s.id === selectedStudent.id
              ? { ...s, coins: (s.coins || 0) + amount }
              : s
          )
        );
      } 
    } catch (error) {
      console.error(error);
      alert("❌ Erro na conexão com o servidor");
    } finally {
      setLoading(false);
    }
  };

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={staggerContainer}
      className="p-6 max-w-4xl mx-auto space-y-6"
    >
      <motion.h1 variants={fadeUp} className="text-3xl font-bold">
        Enviar Moedas para Alunos
      </motion.h1>
      <Link href="/Professor/Dashboard">
        <button className="px-4 py-2 rounded-lg bg-blue-500 text-white hover:bg-blue-600 transition flex items-center gap-2">
          ← Voltar ao Dashboard
        </button>
      </Link>

      {professor ? (
        <p className="text-gray-700">
          Professor logado: <strong>{professor.name}</strong> (
          {professor.login})
        </p>
      ) : (
        <p className="text-red-500">Carregando informações do professor...</p>
      )}

      <motion.div
        variants={fadeUp}
        className="bg-white shadow-lg rounded-xl p-5 divide-y"
      >
        {students.length > 0 ? (
          students.map((student) => (
            <div
              key={student.id}
              className="flex items-center justify-between py-4"
            >
              <div>
                <div className="font-semibold text-lg">{student.name}</div>
                <div className="text-sm text-gray-500">
                  Saldo atual do aluno: {student.coins ?? 0} moedas
                </div>
              </div>
              <button
                onClick={() => openModal(student)}
                className="px-4 py-2 rounded-lg bg-amber-500 text-white hover:bg-amber-600 transition"
              >
                Enviar moedas
              </button>
            </div>
          ))
        ) : (
          <p className="text-center text-gray-500 py-6">
            Nenhum aluno encontrado.
          </p>
        )}
      </motion.div>

      {modalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex items-center justify-center z-50">
          <motion.div
            initial={{ opacity: 0, scale: 0.93, y: -8 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            className="bg-white w-full max-w-md p-6 rounded-xl shadow-xl relative"
          >
            <button
              className="absolute top-3 right-3 hover:bg-gray-100 rounded-full p-1"
              onClick={() => setModalOpen(false)}
            >
              <X size={20} />
            </button>

            <h2 className="text-xl font-semibold mb-2">
              Enviar moedas para {selectedStudent?.name}
            </h2>
            <p className="text-sm text-gray-600 mb-4">
              Informe a quantidade de moedas que deseja enviar.
            </p>

            <input
              type="number"
              min={1}
              value={amount}
              onChange={(e) => setAmount(Number(e.target.value))}
              className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-400"
            />

            <div className="mt-5 flex justify-end gap-3">
              <button
                onClick={() => setModalOpen(false)}
                className="px-4 py-2 rounded-lg bg-gray-200 hover:bg-gray-300 transition"
              >
                Cancelar
              </button>

              <button
                onClick={sendCoins}
                disabled={loading || amount <= 0}
                className="px-4 py-2 rounded-lg bg-amber-500 text-white hover:bg-amber-600 transition disabled:opacity-50"
              >
                {loading ? "Enviando..." : "Confirmar envio"}
              </button>
            </div>
          </motion.div>
        </div>
      )}
    </motion.div>
  );
};

export default SendCoins;
