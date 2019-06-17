export class User {
  id: number;
  username: string;
  password: string;
  firstname: string;
  lastname: string;
  role: string;
  authorities: String[];
  enabled: boolean;
  token?: string;
  basicAuth: string;
}

class Authority {
  id: String;
  authority: String;
}
