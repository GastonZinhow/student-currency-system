export type ProfessorDTO = {
  id: number;
  name: string;
};

export type StudentDTO = {
  id: number;
  name: string;
};

export type TransactionDTO = {
  id: number;
  professor: ProfessorDTO;
  student: StudentDTO;
  amount: number;
  reason: string;
  createdAt: string;
};
