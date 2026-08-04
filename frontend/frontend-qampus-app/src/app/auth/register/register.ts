import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { User } from '../user';
import { AuthService } from '../auth-service';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  constructor(
    private authService: AuthService
  ){}
  registerForm = new FormGroup({
    username: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
    confirmPassword: new FormControl('', Validators.required),
    role: new FormControl('', Validators.required)
  })

  submit(){
    if(this.registerForm.valid){
      const user: User = {
        username: this.registerForm.value.username!,
        email: this.registerForm.value.email!,
        password: this.registerForm.value.password!,
        role: this.registerForm.value.role!
      }
      this.authService.register(user);
    }
  }
}
