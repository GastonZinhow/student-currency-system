export type Transaction = {
id: string;
date: string;
description: string;
type: "GAIN" | "SPEND";
amount: number;
};

export interface Notification {
id: number;
sender: string;        // remetente
type: "GAIN" | "SPEND"; // tipo de transação
amount: number;        // quantidade de moedas
date: string;          // data em ISO ou string
read: boolean;         // se a notificação foi lida
}

export const student = {
name: "Mariana Silva",
balance: 420,
summary: {
  last7DaysGained: 80,
  exchanges: 12,
},
transactions: Array.from({ length: 8 }).map((_, i) => {
  const date = new Date();
  date.setDate(date.getDate() - i);
  return {
    id: String(i + 1),
    date: date.toISOString(),
    description: i % 2 === 0 ? "Recebido de Prof. João" : "Troca por Vantagem A",
    type: i % 2 === 0 ? "GAIN" : "SPEND",
    amount: i % 2 === 0 ? 20 : 30,
  } as Transaction;
}),
notifications: [
  {
    id: 1,
    sender: "Prof. João",
    type: "GAIN",
    amount: 20,
    date: new Date().toISOString(),
    read: false,
  },
  {
    id: 2,
    sender: "Prof. Ana",
    type: "GAIN",
    amount: 10,
    date: new Date(Date.now() - 86400000).toISOString(),
    read: true,
  },
] as Notification[],
};

export const partner = {
name: "Loja Parceira X",
rewardsCount: 6,
totalExchanges: 123,
popularReward: "Camiseta Oficial",
exchangesRecent: [
  { id: "e1", student: "Mariana Silva", reward: "Camiseta Oficial", date: new Date().toISOString(), status: "Confirmado" },
  { id: "e2", student: "Lucas Alves", reward: "Café Grátis", date: new Date(Date.now() - 3600 * 1000 * 5).toISOString(), status: "Pendente" },
],
};