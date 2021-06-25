import { Injectable } from '@angular/core';
import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { SpinnerOverlayComponent } from 'src/app/spinner-overlay/spinner-overlay.component';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpinnerOverlayService {
  private overlayRef: OverlayRef = undefined;
  public isLoading:BehaviorSubject<boolean>=new BehaviorSubject<boolean>(false);
  constructor() {}
  
}
