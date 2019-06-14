import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {AuthGuard} from "./_guards/auth.guard";
import {HomeComponent} from "./home/home.component";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard],
    data: {title: 'Strona główna'}
  },
  // {
  //   path: 'admin',
  //   component: AdminComponent,
  //   canActivate: [AuthGuard],
  //   data: {roles: [Role.Admin]}
  // },
  {
    path: 'login',
    component: LoginComponent
  },
  // {
  //   path: 'debts',
  //   component: DebtComponent,
  //   canActivate: [AuthGuard],
  //   data: {roles: [Role.User]},
  //   runGuardsAndResolvers: 'always'
  // },
  // { path: 'register', component: RegisterComponent },

  // otherwise redirect to home
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
