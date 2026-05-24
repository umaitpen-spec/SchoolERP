import { useAuthStore } from '@/store/authStore';
import type { Role } from '@/types';

export function useRole() {
  const user = useAuthStore((s) => s.user);
  const role = user?.role ?? null;

  return {
    role,
    isAdmin: role === 'ADMIN',
    isTeacher: role === 'TEACHER',
    isStudent: role === 'STUDENT',
    isParent: role === 'PARENT',
    hasRole: (r: Role) => role === r,
    hasAnyRole: (...roles: Role[]) => !!role && roles.includes(role),
  };
}
