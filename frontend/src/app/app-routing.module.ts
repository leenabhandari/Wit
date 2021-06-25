import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InputFormComponent } from './input-form/input-form.component';
import { OutputDashboardComponent } from './output-dashboard/output-dashboard.component';

const routes: Routes = [
  {path:'home',component:InputFormComponent},
  {path:'result',component:OutputDashboardComponent},
  {path:'**',component:InputFormComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
