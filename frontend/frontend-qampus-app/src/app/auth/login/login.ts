import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../auth-service';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  email = '';
  password = '';

  constructor(private authService: AuthService) {}

  async onSubmit() {
    console.log('Email:', this.email);
    console.log('Senha:', this.password);
  }
}