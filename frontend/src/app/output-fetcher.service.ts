import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class OutputFetcherService {
  outputData: any;
  

  constructor(
    private http: HttpClient
  ) { }

  getOutput(data:any){
    return this.http.post("https://wit-agri.herokuapp.com/?area="+data.area+"&lat="+data.latitude+"&lon="+data.longitude+"&soil.k="+data.K+"&soil.n="+data.N+"&soil.p="+data.P+"&soil.ph="+data.ph,{});
  }
  showOutput(){
    return localStorage.getItem("farmoResponse");
  }

}


