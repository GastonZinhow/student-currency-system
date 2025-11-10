"use client";

import React, { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import { Toast } from "@/components/ui/toast";
import { motion } from "framer-motion";
import { Coins } from "lucide-react";
import api from "@/services/api";
import { authService } from "@/services/authService";
import { fadeUp } from "@/components/motionVariants";
import { useRouter } from "next/navigation";

export default function TrocarVantagem() {
  const [openDialog, setOpenDialog] = useState(false);
  const [vantagens, setVantagens] = useState<any[]>([]);
  const [vantagemSelecionada, setVantagemSelecionada] = useState<any>(null);
  const [toastMessage, setToastMessage] = useState<string | null>(null);
  const [coins, setCoins] = useState<number>(0);
  const [loading, setLoading] = useState(false);

  const user = authService.getUser();
  const router = useRouter();

  const handleTrocar = (vantagem: any) => {
    setVantagemSelecionada(vantagem);
    setOpenDialog(true);
  };

  const confirmarTroca = async () => {
    if (!user?.userId) return;
    
    setLoading(true);
    try {
      await api.post(`/redemptions/redeem`, {
        advantageId: vantagemSelecionada.id,
        studentId: user.userId,
      });

      const { data: updatedStudent } = await api.get(`/students/${user.userId}`);

      const currentUser = authService.getUser();
      const updatedUser = {
        ...currentUser,
        coins: updatedStudent.coins
      };
      localStorage.setItem("user", JSON.stringify(updatedUser));

      setCoins(updatedStudent.coins);

      setToastMessage("🎉 Troca realizada com sucesso!");
      setOpenDialog(false);

      setTimeout(() => {
        router.push("/Aluno/Dashboard");
      }, 1500);

    } catch (error) {
      console.error(error);
      setToastMessage("❌ Erro ao realizar troca");
    } finally {
      setLoading(false);
    }
  };

  const fetchVantagens = async () => {
    try {
      const response = await api.get("/advantages");
      setVantagens(response.data);
    } catch (error) {
      console.error("Erro ao buscar vantagens:", error);
    }
  };

  useEffect(() => {
    if (user?.userId) {
      fetchVantagens();
      
      api.get(`/students/${user.userId}`)
        .then(({ data }) => setCoins(data.coins))
        .catch(err => console.error("Erro ao buscar moedas:", err));
    }
  }, [user?.userId]);

  return (
    <div className="p-6 max-w-6xl mx-auto space-y-6">
      <h1 className="text-3xl font-bold mb-2">Trocar Moedas por Vantagem</h1>
      <p className="text-gray-500">
        Você possui <span className="font-semibold">{coins}</span> moedas.
        Escolha uma vantagem abaixo!
      </p>

      <motion.div variants={fadeUp}>
        <button
          onClick={() => router.push("/Aluno/Dashboard")}
          className="px-4 py-2 mb-4 text-sm font-medium text-white bg-blue-600 rounded hover:bg-blue-700 transition"
        >
          ← Voltar ao Início
        </button>
      </motion.div>

      {/* Catálogo de Vantagens */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {vantagens.map((v) => (
          <motion.div
            key={v.id}
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.97 }}
            transition={{ duration: 0.2 }}
          >
            <Card className="hover:shadow-lg transition-shadow">
              <CardHeader className="flex flex-col items-center space-y-2">
                <div className="p-4 bg-gray-100 rounded-full">
                  <img
                    src={v.picture}
                    alt={v.name}
                    className="w-20 h-20 rounded-full object-cover"
                  />
                </div>
                <CardTitle>{v.name}</CardTitle>
                <CardDescription className="text-center">
                  {v.description}
                </CardDescription>
              </CardHeader>

              <CardContent className="flex justify-between items-center mt-2">
                <div className="flex items-center gap-2 font-semibold text-lg text-gray-700">
                  <Coins size={18} className="text-yellow-400" />
                  {v.cost} moedas
                </div>
                <Button
                  onClick={() => handleTrocar(v)}
                  disabled={v.cost > coins}
                  className="bg-blue-500 hover:bg-blue-600 text-white transition-transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  Trocar
                </Button>
              </CardContent>
            </Card>
          </motion.div>
        ))}
      </div>

      {/* Modal de Confirmação */}
      <Dialog open={openDialog} onOpenChange={setOpenDialog}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Confirmar Troca</DialogTitle>
            <DialogDescription>
              {vantagemSelecionada &&
                `Deseja trocar ${vantagemSelecionada.cost} moedas por "${vantagemSelecionada.name}"?`}
            </DialogDescription>
          </DialogHeader>
          <DialogFooter>
            <Button
              onClick={confirmarTroca}
              disabled={loading}
              className="bg-emerald-500 hover:bg-emerald-600"
            >
              {loading ? "Processando..." : "Confirmar troca"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Toast */}
      {toastMessage && <Toast message={toastMessage} type="success" />}
    </div>
  );
}