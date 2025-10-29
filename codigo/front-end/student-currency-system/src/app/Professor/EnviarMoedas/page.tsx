"use client";

import React, { useState } from "react";
import { motion } from "framer-motion";
import { fadeUp, staggerContainer } from "@/components/motionVariants";
import { X } from "lucide-react";
import Link from "next/link";

const mockStudents = [
  { id: 1, name: "Ana Souza", balance: 120 },
  { id: 2, name: "Carlos Pereira", balance: 80 },
  { id: 3, name: "João Mendes", balance: 50 },
];

const SendCoins = () => {
  const [selectedStudent, setSelectedStudent] = useState<any>(null);
  const [amount, setAmount] = useState<number>(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  const openModal = (student: any) => {
    setSelectedStudent(student);
    setAmount(0);
    setModalOpen(true);
  };

  const sendCoins = () => {
    setLoading(true);

    // ATUALIZAR QUANDO BACK ESTIVER ON 
    setTimeout(() => {
      setLoading(false);
      setModalOpen(false);
      alert(`✅ Você enviou ${amount} moedas para ${selectedStudent.name}`);
    }, 700);
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

      <motion.div
        variants={fadeUp}
        className="bg-white shadow-lg rounded-xl p-5 divide-y"
      >
        {mockStudents.map((student) => (
          <div
            key={student.id}
            className="flex items-center justify-between py-4"
          >
            <div>
              <div className="font-semibold text-lg">{student.name}</div>
              <div className="text-sm text-gray-500">
                Saldo atual do aluno: {student.balance} moedas
              </div>
            </div>
            <button
              onClick={() => openModal(student)}
              className="px-4 py-2 rounded-lg bg-amber-500 text-white hover:bg-amber-600 transition"
            >
              Enviar moedas
            </button>
          </div>
        ))}
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
