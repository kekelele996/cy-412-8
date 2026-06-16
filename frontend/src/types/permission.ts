import type { UserRole } from '../constants/user';

export type PermissionCode =
  | 'dashboard:view'
  | 'repair:view'
  | 'repair:create'
  | 'repair:assign'
  | 'repair:update'
  | 'payment:view'
  | 'payment:pay'
  | 'announcement:view'
  | 'announcement:publish'
  | 'user:profile'
  | 'operationLog:view'
  | 'complaint:view'
  | 'complaint:create'
  | 'complaint:reply'
  | 'complaint:resolve';

export interface RoutePermissionMeta {
  permission: PermissionCode;
  roles?: UserRole[];
}
