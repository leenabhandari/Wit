import { Component } from '@angular/core';
import { SpinnerOverlayService } from './spinner-overlay.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  public constructor(public loaderService:SpinnerOverlayService){}
  title = 'Farm-O-Pedia';
}
