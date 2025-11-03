"use client";

import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Button } from "@/components/ui/button";
import { motion } from "framer-motion";
import { fadeUp, staggerContainer } from "@/components/motionVariants";
import { useRouter } from "next/navigation";

export default function CadastrarVantagem() {
  const [nome, setNome] = useState("");
  const [descricao, setDescricao] = useState("");
  const [custo, setCusto] = useState<number | undefined>(undefined);
  const [quantidade, setQuantidade] = useState<number | undefined>(undefined);

  const [selectedCompany, setSelectedCompany] = useState<any>(null);
  const [loading, setLoading] = useState(false);
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
        .catch((err) => console.error("Erro ao buscar professor:", err));
    }
  }, []);

  const handleSalvar = async () => {
    if (!selectedCompany) {
      alert("Empresa não identificada. Faça login novamente.");
      return;
    }

    setLoading(true);
    try {
      const response = await fetch("http://localhost:8080/advantages", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          companyId: selectedCompany.id,
          name: nome,
          description: descricao,
          cost: custo,
          amount: quantidade,
          picture: "test"
        }),
      });

      if (response.ok) {
        alert("✅ Vantagem cadastrada com sucesso!");
      }

    } catch (error) {
      console.error(error);
      alert("Erro na conexão com o servidor");
    } finally {
      setLoading(false);
      router.push("/EmpresaParceira/Dashboard");
    }
  };

  return (
    <motion.div initial="hidden" animate="visible" variants={staggerContainer} className="max-w-xl mx-auto mt-10 p-6 space-y-6">
      <motion.h1 variants={fadeUp} className="text-3xl font-bold text-center">
        Cadastrar Vantagem
      </motion.h1>

      <Card>
        <CardHeader>
          <CardTitle>Preencha os dados da vantagem</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">

          <div>
            <Label>Nome da vantagem</Label>
            <Input value={nome} onChange={(e) => setNome(e.target.value)} />
          </div>

          <div>
            <Label>Descrição</Label>
            <Textarea value={descricao} onChange={(e) => setDescricao(e.target.value)} />
          </div>

          <div>
            <Label>Custo em moedas</Label>
            <Input type="number" value={custo} onChange={(e) => setCusto(Number(e.target.value))} />
          </div>

          <div>
            <Label>Quantidade disponível</Label>
            <Input type="number" value={quantidade} onChange={(e) => setQuantidade(Number(e.target.value))} />
          </div>

          <div className="flex justify-end gap-3 mt-4">
            <Button variant="outline" onClick={() => router.push("/EmpresaParceira/Dashboard")}>
              Cancelar
            </Button>
            <Button onClick={handleSalvar} disabled={loading}>
              {loading ? "Salvando..." : "Salvar"}
            </Button>
          </div>

        </CardContent>
      </Card>
    </motion.div>
  );
}
