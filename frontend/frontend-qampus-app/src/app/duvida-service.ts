import { Injectable } from '@angular/core';
import { Duvida } from './duvida';

@Injectable({
  providedIn: 'root',
})
export class DuvidaService {
  private apiUrl = "http://localhost:8080/question";

  async createDuvida(duvida: Duvida): Promise<boolean>{
    try{
      const response = await fetch(this.apiUrl+"/create", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer '+localStorage.getItem('token')
        },
        body: JSON.stringify(duvida)
      })
      return response.ok;
    }catch(error){
      console.error('Error creating new Post: ', error);
      return false;
    }
  }
}
