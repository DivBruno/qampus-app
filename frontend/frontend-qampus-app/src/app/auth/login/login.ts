import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  email = '';
  senha = '';

  onSubmit() {
    console.log('Email:', this.email);
    console.log('Senha:', this.senha);
  }
}