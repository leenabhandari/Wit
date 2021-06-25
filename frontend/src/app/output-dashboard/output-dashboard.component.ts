import { Component, OnInit } from '@angular/core';
import { OutputFetcherService } from '../output-fetcher.service';

@Component({
  selector: 'app-output-dashboard',
  templateUrl: './output-dashboard.component.html',
  styleUrls: ['./output-dashboard.component.css']
})
export class OutputDashboardComponent implements OnInit {

  constructor(
    private outputFetcher:OutputFetcherService
  ) { }

  outputData:any;

  ngOnInit(): void {
    this.getResults();
  
  }
  getResults(): void{
    this.outputData=JSON.parse(this.outputFetcher.showOutput());
    console.log(this.outputData);
  }
  goToURL(URL:string){
    alert(URL);
    window.open(URL, "_blank");
  }
  

}
