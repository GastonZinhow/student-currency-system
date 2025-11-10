import { StudentDTO } from "./transactionData";

export interface RedemptionDTO {
  id: number;
  studentId: number;
  advantage: {
    id: number;
    name: string;
    cost: number;
  };
  createdAt: string;
}