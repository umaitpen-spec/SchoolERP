export type Role = 'ADMIN' | 'TEACHER' | 'STUDENT' | 'PARENT';
export type Gender = 'MALE' | 'FEMALE' | 'OTHER';
export type AttendanceStatus = 'PRESENT' | 'ABSENT' | 'LATE' | 'EXCUSED';
export type ExamType = 'UNIT_TEST' | 'MID_TERM' | 'FINAL' | 'PRACTICAL' | 'ASSIGNMENT' | 'QUIZ';
export type FeeStatus = 'PENDING' | 'PAID' | 'OVERDUE' | 'PARTIAL' | 'WAIVED';
export type SchoolDay = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY';
export type NotificationType = 'GENERAL' | 'ATTENDANCE' | 'EXAM' | 'FEE' | 'TIMETABLE' | 'RESULT';

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

export interface User {
  id: number;
  email: string;
  fullName: string;
  profileImageUrl?: string;
  role: Role;
  gender?: Gender;
  phoneNumber?: string;
  active: boolean;
  lastLogin?: string;
  createdAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user?: User;
}

export interface Student {
  id: number;
  userId: number;
  fullName: string;
  email: string;
  profileImageUrl?: string;
  rollNumber: string;
  admissionNumber: string;
  dateOfBirth?: string;
  address?: string;
  bloodGroup?: string;
  admissionDate?: string;
  phoneNumber?: string;
  gender?: Gender;
  classRoomId?: number;
  className?: string;
  section?: string;
  parentId?: number;
  parentName?: string;
  createdAt: string;
}

export interface Teacher {
  id: number;
  userId: number;
  fullName: string;
  email: string;
  profileImageUrl?: string;
  employeeId?: string;
  qualification?: string;
  specialization?: string;
  yearsOfExperience?: number;
  address?: string;
  phoneNumber?: string;
  gender?: Gender;
  subjectNames?: string[];
  createdAt: string;
}

export interface Attendance {
  id: number;
  studentId: number;
  studentName: string;
  rollNumber: string;
  classRoomId?: number;
  className?: string;
  subjectId?: number;
  subjectName?: string;
  teacherId?: number;
  teacherName?: string;
  attendanceDate: string;
  status: AttendanceStatus;
  remarks?: string;
  createdAt: string;
}

export interface AttendanceSummary {
  present: number;
  absent: number;
  late: number;
  excused: number;
}

export interface Exam {
  id: number;
  examName: string;
  examType: ExamType;
  subjectId: number;
  subjectName: string;
  classRoomId: number;
  className: string;
  examDate: string;
  startTime?: string;
  endTime?: string;
  totalMarks: number;
  passingMarks: number;
  academicYear: string;
  description?: string;
}

export interface Marks {
  id: number;
  studentId: number;
  studentName: string;
  rollNumber: string;
  examId: number;
  examName: string;
  subjectName: string;
  marksObtained: number;
  totalMarks: number;
  percentage: number;
  grade?: string;
  remarks?: string;
  absent: boolean;
}

export interface Fees {
  id: number;
  studentId: number;
  studentName: string;
  rollNumber: string;
  feeType: string;
  amount: number;
  amountPaid: number;
  balance: number;
  dueDate: string;
  paidDate?: string;
  status: FeeStatus;
  academicYear: string;
  term?: string;
  transactionId?: string;
  remarks?: string;
  createdAt: string;
}

export interface Timetable {
  id: number;
  classRoomId: number;
  className: string;
  section: string;
  subjectId: number;
  subjectName: string;
  teacherId: number;
  teacherName: string;
  dayOfWeek: SchoolDay;
  startTime: string;
  endTime: string;
  academicYear: string;
  roomNumber?: string;
}

export interface Notification {
  id: number;
  title: string;
  message: string;
  type: NotificationType;
  read: boolean;
  referenceId?: number;
  createdAt: string;
}
