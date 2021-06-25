import { Component, OnInit } from '@angular/core';
import { InputParameters } from '../InputParameters';
import { OutputFetcherService } from '../output-fetcher.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SpinnerOverlayService } from '../spinner-overlay.service';
@Component({
  selector: 'app-input-form',
  templateUrl: './input-form.component.html',
  styleUrls: ['./input-form.component.css']
})
export class InputFormComponent implements OnInit {

  constructor(
    private outputFetcherService:OutputFetcherService,
    private router:Router ) { }

  data=new InputParameters();
  lat = 12.9716;
  lon = 77.5946;
  ngOnInit(): void {
  }
  onSubmit() {
    
    this.outputFetcherService.getOutput(this.data).subscribe((success)=>{
      //this.outputData=success;
      localStorage.setItem("farmoResponse",JSON.stringify(success));
      this.router.navigateByUrl('/result');
      console.log(success);
    },(error)=>{
      //this.outputData=error;
    });
    
    
  }
  setLocation() {
    this.data.latitude=12.9716;
    this.data.longitude=77.5946;
  }

  setSoil() {
    this.data.N=90;
    this.data.K=43;
    this.data.P=42;
    this.data.ph=6.733;
  }

  

}
